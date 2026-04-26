package ro.pyc22.shop.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.Category;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.model.ProductImage;
import ro.pyc22.shop.model.modelDTO.ProductWithImagesDto;
import ro.pyc22.shop.repositories.CategoryRepository;
import ro.pyc22.shop.repositories.ProductRepository;
import ro.pyc22.shop.services.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService<Product> {
    @Value("${upload.folder}")
    private String uploadFolder;

    private final ProductRepository<Product> productRepository;
    private final CategoryRepository<Category> categoryRepository;


    @Override
    public List<Product> getAllProducts() {

        return productRepository.getAllProducts();
    }

    @Override
    public Product createProduct(String product,String[] imagesPaths, String categorySlug) {

        // create object form string
        ObjectMapper mapper = new ObjectMapper();
        Product newProduct;

        try{
            newProduct = mapper.readValue(product,Product.class);
        }
        catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }

        Product createdProduct = this.productRepository.createProduct(newProduct);

        for(String path : imagesPaths ){
           this.productRepository.savePath(createdProduct.getId(), path);
        }

        //insert product in category_product table
        this.categoryRepository.linkProductToCategoryBySlug(createdProduct, categorySlug);

        return newProduct;

    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }


    @Override
    public List<ProductWithImagesDto> getProductsByCategory(String slug) {

        //is not implement any verification of category(if is active or else)
        Long categoryId = categoryRepository.getCategoryBySlug(slug).getId();

        //get product
        List<Product> productList=  productRepository.getProductsByCategory(categoryId);
        return getProductsWithImages( productList);

    }

    private List<ProductWithImagesDto> getProductsWithImages(List<Product> productsList) {
        List<Long> productsIds = productsList.stream().map(Product::getId).toList();

        List<ProductImage> allImages = productRepository.findImagesForProductIds(productsIds);

        Map<Long,List<ProductImage>> imagesByProductId = allImages.stream().collect(
                Collectors.groupingBy(ProductImage :: getProduct_Id, LinkedHashMap::new,Collectors.toList()));

        return  productsList.stream()
                .map(p -> {
                    List<String> paths = imagesByProductId.getOrDefault(p.getId(), List.of())
                            .stream()
                            .map(ProductImage::getPath)
                            .toList();
                    return new ProductWithImagesDto(p, paths);
                })
                .toList();


    }

    @Override
    public List<Product> getProductByProperties() {
        return List.of();
    }

    @Override
    public ProductWithImagesDto getProductWithImages(String id) {
        return null;
    }

    @Override
    public List<ProductWithImagesDto> getAll() {
        return List.of();
    }

    @Override
    public List<String> uploadFiles(MultipartFile[] files, String category, String productCode) {
        List<String> savedPath = new ArrayList<>();

        try{
            String folderPath = uploadFolder + category + "/" + productCode;
            Path folder = Paths.get(folderPath);

            if(!Files.exists(folder)){
                Files.createDirectories(folder);
            }

            for(MultipartFile file : files){
                Path filePath = folder.resolve(file.getOriginalFilename());
                file.transferTo(filePath.toFile());


                String baseUrl = "http://localhost:8081/images/";
                String relativePath = category + "/" + productCode  + "/" + file.getOriginalFilename();
                savedPath.add(baseUrl + relativePath);


            }
            return  savedPath;


        } catch (IOException e) {
            throw new RuntimeException("Upload fail " + e.getMessage());
        }
    }

    @Override
    public ProductWithImagesDto getProductWithImagesById(String stringId) {
        try{
            Long id = Long.valueOf(stringId);
            Product product = this.productRepository.getProductById(id);
            log.info("Avem produs -?  {}", product.getProductCode());
            List<String> imagesPaths = this.productRepository.getImagesPathsByProductId(id);
            return new ProductWithImagesDto(product,imagesPaths);

        }catch(NumberFormatException nfe){
            throw new ApiException(nfe.getMessage());
        }

    }

    //used for all products
    @Override
    public List<ProductWithImagesDto> getAllProductsWithImages() {
        List<Product> products = getAllProducts();
        return getProductsWithImages(products);

    }

}
