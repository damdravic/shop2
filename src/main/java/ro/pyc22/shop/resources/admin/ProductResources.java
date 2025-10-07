package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.services.ProductService;

import java.util.Map;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@Slf4j
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






  @GetMapping("/productsWithImages")
  public ResponseEntity<HttpResponse> getAllProductsWithImages(){
        log.info("in resource productsWithImages");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .statusCode(OK.value())
                        .data(Map.of("productsWithImages" , productService.getAllProductsWithImages()))
                        .build());
  }






    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/uploadFiles")
     //here come images with category and product code and return path array
    public ResponseEntity<HttpResponse> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                       @RequestParam("category") String category,
                                                       @RequestParam("productCode") String productCode){
        log.info("in resource");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(OK)
                        .timeStamp(now().toString())
                        .developerMessage("")
                        .message("")
                        .data(Map.of("filePath",productService.uploadFiles(files,category,productCode)))
                        .build());

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/addProduct")
    public ResponseEntity<HttpResponse> addProduct(@RequestParam("product") String product,
                                                      @RequestParam("imagesPaths") String[] imagesPaths){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(Map.of("product",productService.createProduct(product,imagesPaths))).build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getProductWithImages/{id}")
    public ResponseEntity<HttpResponse> getProductWithImagesById(@PathVariable("id") String id){

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(Map.of("productWithImages",productService.getProductWithImagesById(id))).build());
    }





}
