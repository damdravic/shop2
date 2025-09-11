package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.services.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductResources {


    private final ProductService<Product> productService;



    @GetMapping("/products")
    public ResponseEntity<HttpResponse> getAllProducts (){
      return ResponseEntity.ok().body(
              HttpResponse.builder()
                      .httpStatus(HttpStatus.OK)
                      .statusCode(HttpStatus.OK.value())
                      .message("Products")
                      .data(Map.of("products",productService.getAllProducts()))
                      .build()
      );
  }




}
