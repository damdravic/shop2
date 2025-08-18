package ro.pyc22.shop.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResources {

    @GetMapping("/public")
    public String pub(){
        return "public working";
    }


    @GetMapping("/secure")
    public String sec(){
        return "secure working";
    }
}
