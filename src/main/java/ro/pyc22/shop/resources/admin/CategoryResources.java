package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pyc22.shop.model.Category;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.services.CategoryService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/category")
@Slf4j
public class CategoryResources {

    private final CategoryService categoryService;


    @GetMapping("/getAllCategories")
    public ResponseEntity<HttpResponse> getAllCategories (){
        System.out.println("in resources GetAllCategories");
         return ResponseEntity.ok().body(
              HttpResponse.builder()
                      .httpStatus(HttpStatus.OK)
                      .statusCode(HttpStatus.OK.hashCode())
                      .data(Map.of("categories",categoryService.getAllCategories()))
                      .build()
         );

    }

    @PostMapping("/addNewCategory")
    public ResponseEntity<HttpResponse> addNewCategory(@RequestBody Category category){

        log.info("Category description {}", category.getCategoryDescription());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("category",categoryService.createCategory(category)))

                        .build()
        );
    }



}
