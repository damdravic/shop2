package ro.pyc22.shop.repositories;

import ro.pyc22.shop.model.CartProduct;
import ro.pyc22.shop.model.modelDTO.CartDto;

 public interface CartRepository {


    Long checkCart(long user_id);

    CartDto getCart(long userId);

    CartDto updateCart(long userId, CartProduct cartProduct);

    CartDto createCart(long userId, CartProduct cartProduct);

    CartDto deleteCartProduct(long userId, long productId);

    CartDto decrementQuantity(long userId, long productId);

     CartDto mergeCart(long userId, CartProduct[] guestCart);
 }
