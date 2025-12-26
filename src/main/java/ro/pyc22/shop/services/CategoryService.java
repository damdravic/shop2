package ro.pyc22.shop.services;

import ro.pyc22.shop.model.Category;

import java.util.List;

public interface CategoryService{


    Category createCategory(Category category);

    List<Category> getAllCategories();

    Category deleteCategory(Long id);


}
