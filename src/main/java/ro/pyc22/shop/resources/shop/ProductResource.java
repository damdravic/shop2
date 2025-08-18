package ro.pyc22.shop.resources.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.Product;

@RestController
@RequestMapping("/shop")
public class ProductResource {

    @GetMapping("/getAllProducts")
    public Product getProduct(){
        return new Product();
    }




}
