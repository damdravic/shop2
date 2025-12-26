package ro.pyc22.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.Category;
import ro.pyc22.shop.repositories.CategoryRepository;
import ro.pyc22.shop.services.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository<Category> categoryRepository;


    @Override
    public Category createCategory(Category category) {
      return   categoryRepository.createCategory(category);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> category = categoryRepository.getAllCategories();

        category.forEach( cat -> System.out.println(cat.getCategoryName()));




        return categoryRepository.getAllCategories();
    }


    @Override
    public Category deleteCategory(Long id) {
        return null;
    }


}
