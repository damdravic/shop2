package ro.pyc22.shop.services;

import ro.pyc22.shop.model.Product;

import java.util.List;

public interface ProductService<T extends Product> {


    List<T> getAllProducts();

    T createProduct( Product product);

    T getProductById( Long id);

    List<T> getProductsByCategoryId( Long categoryId);



}
