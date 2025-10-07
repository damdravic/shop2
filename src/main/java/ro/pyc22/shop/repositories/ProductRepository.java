package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.model.ProductImage;

import java.util.List;

public interface ProductRepository<T extends Product> {

    List<T> getAllProducts();

    T createProduct(Product product);

    T getProductById( Long id);

    List<String> getImagesPathsByProductId(Long id);

    String savePath(Long id, String path);


    List<ProductImage> findImagesForProductIds(List<Long> productsIds);
}
