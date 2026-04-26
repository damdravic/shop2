package ro.pyc22.shop.services;

import org.springframework.web.multipart.MultipartFile;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.model.modelDTO.ProductWithImagesDto;

import java.util.List;

public interface ProductService<T extends Product> {



    Product createProduct(String productJSON, String[] Paths, String categorySlug);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<ProductWithImagesDto> getProductsByCategory(String slug);

    List<Product>  getProductByProperties();

    ProductWithImagesDto getProductWithImages(String id);

    List<ProductWithImagesDto> getAll();

    List<String> uploadFiles(MultipartFile[] files, String category, String productCode);

    ProductWithImagesDto getProductWithImagesById(String id);

    List<ProductWithImagesDto> getAllProductsWithImages();
}
