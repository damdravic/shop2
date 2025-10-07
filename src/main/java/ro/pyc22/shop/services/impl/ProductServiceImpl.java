package ro.pyc22.shop.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.model.ProductImage;
import ro.pyc22.shop.model.modelDTO.ProductWithImagesDto;
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


    @Override
    public List<Product> getAllProducts() {

        return productRepository.getAllProducts();
    }

    @Override
    public Product createProduct(String product,String[] imagesPaths) {

        // create object form string
        ObjectMapper mapper = new ObjectMapper();
        Product newProduct;

        try{
            newProduct = mapper.readValue(product,Product.class);
        }
        catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }

        //we have product from db...need id to insert paths in db
        log.info(newProduct.toString());
        Product createdProduct = this.productRepository.createProduct(newProduct);
        System.out.println(createdProduct.toString());

        List<String> savedPaths = new ArrayList<>();
        for(String path : imagesPaths ){
            savedPaths.add(this.productRepository.savePath(createdProduct.getId(), path));}

        return newProduct;

    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }


    @Override
    public List<Product> getProductsByCategory() {
        return List.of();
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

    @Override
    public List<ProductWithImagesDto> getAllProductsWithImages() {
        List<Product> products = getAllProducts();
        List<Long> productsIds = products.stream().map(Product::getId).toList();
        List<ProductImage> allImages = productRepository.findImagesForProductIds(productsIds);

          Map<Long,List<ProductImage>> imagesByProductId = allImages.stream().collect(
                  Collectors.groupingBy(ProductImage::getProduct_Id, LinkedHashMap::new, Collectors.toList()));

        List< ProductWithImagesDto > pwi = products.stream()
                .map(p -> {
                    List<String> paths = imagesByProductId.getOrDefault(p.getId(), List.of())
                            .stream()
                            .map(ProductImage::getPath)
                            .toList();
                    return new ProductWithImagesDto(p, paths);
                })
                .toList();
        log.info(pwi.stream().toString());
        return  pwi;
    }

}
