package ro.pyc22.shop.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.CartProduct;
import ro.pyc22.shop.model.User;

import ro.pyc22.shop.model.modelDTO.CartDto;
import ro.pyc22.shop.repositories.CartRepository;
import ro.pyc22.shop.repositories.UserRepository;
import ro.pyc22.shop.services.CartService;

@RequiredArgsConstructor
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository<User> userRepository;



    @Override
    public CartDto addProduct(CartProduct cartProduct) {
      long userId = getCurrentUserId();
      return  cartRepository.updateCart(userId,cartProduct);

    }

    @Override
    public CartDto deleteCartProduct( long productId) {
        long userId = getCurrentUserId();
        return cartRepository.deleteCartProduct(userId, productId);
    }

    @Override
    public CartDto decrementQuantity( long productId) {
        long userId = getCurrentUserId();
        return cartRepository.decrementQuantity(userId,productId);
    }

    @Override
    public CartDto mergeCart(CartProduct[] guestCart) {
        log.info("merge Carts");
        long userId = getCurrentUserId();
        return cartRepository.mergeCart(userId,guestCart);
    }


    private long getCurrentUserId(){
        //TODO - to set user in authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationCredentialsNotFoundException("not authenticated");
        }

        String email =  authentication.getPrincipal().toString();
        User user = userRepository.getUser(email);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return  user.getId();
    }






}
