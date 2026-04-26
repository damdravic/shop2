package ro.pyc22.shop.services;

import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.CartProduct;
import ro.pyc22.shop.model.modelDTO.CartDto;


public interface CartService {

    CartDto addProduct(CartProduct cartProduct);

    CartDto deleteCartProduct(long productId);

    CartDto decrementQuantity(long productId);


   CartDto mergeCart(CartProduct[] guestCart);
}
