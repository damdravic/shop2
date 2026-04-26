package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.Category;
import ro.pyc22.shop.model.Product;

import java.util.List;

public interface CategoryRepository<T extends  Category>  {

   T createCategory(Category Category);

   T getCategoryBySlug(String categorySlug);


   List<T> getAllCategories();


   //for Product_Category

    void linkProductToCategoryBySlug(Product product,String categorySlug);


}
