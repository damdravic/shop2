package ro.pyc22.shop.resources.shop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.CartProduct;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.services.CartService;

import java.util.Map;

@RestController
@RequestMapping("/shop/cart")
@Slf4j
@RequiredArgsConstructor
public class CartResources {

    private final CartService cartService;

    @PostMapping("/addProduct")
    public ResponseEntity<HttpResponse> addProductToCart(@RequestBody CartProduct cartProduct){
        log.info("in resource");
        log.info( Long.toString( cartProduct.getProductId()));
        return   ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .reason("")
                        .message("")
                        .developerMessage("")
                        .data(Map.of("cart",cartService.addProduct(cartProduct)))

                        .build()
        );
    }

    @PostMapping("/mergeCart")
    public ResponseEntity<HttpResponse> mergeCart(@RequestBody CartProduct[] guestCart){

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .reason("")
                        .message("")
                        .developerMessage("")
                        .data(Map.of("cart",cartService.mergeCart(guestCart)))
                .build()
        );
    }









}
