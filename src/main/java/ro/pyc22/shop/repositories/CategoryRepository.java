package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.Category;

import java.util.List;

public interface CategoryRepository<T extends  Category>  {

   T createCategory(Category Category);

   List<T> getAllCategories();


}
