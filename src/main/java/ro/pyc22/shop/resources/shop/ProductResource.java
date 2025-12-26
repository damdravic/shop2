package ro.pyc22.shop.resources.shop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.services.ProductService;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/shop/public")
@RequiredArgsConstructor
@Slf4j
public class ProductResource {

    private final ProductService<Product> productService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<HttpResponse> getAllProduct(){
      log.info("in resource");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Product load successfully")
                        .data(Map.of("products",productService.getAllProducts()))
                        .build()
        );
    }

    @GetMapping("/productsWithImages")
    public ResponseEntity<HttpResponse> getAllProductsWithImages(){
        log.info("in resource productsWithImages");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .statusCode(OK.value())
                        .data(Map.of("productsWithImages" , productService.getAllProductsWithImages()))
                        .build());
    }




}
