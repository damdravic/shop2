package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.Product;

import java.util.List;

public interface ProductRepository<T extends Product> {

    List<T> getAllProducts();

    T createProduct(Product product);

    T getProductById( Long id);




}
