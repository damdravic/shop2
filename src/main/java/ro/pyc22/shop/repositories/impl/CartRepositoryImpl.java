package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.pyc22.shop.model.CartProduct;
import ro.pyc22.shop.model.modelDTO.CartDto;
import ro.pyc22.shop.repositories.CartRepository;
import ro.pyc22.shop.repositories.rowMappers.CartProductRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ro.pyc22.shop.repositories.queries.CartQueries.*;


@RequiredArgsConstructor
@Slf4j
@Repository
public class CartRepositoryImpl implements CartRepository {

    private final NamedParameterJdbcTemplate jdbc;



    @Override
    public Long checkCart(long userId) {
      return  Objects.requireNonNull(getActiveCart(userId)).getId();
    }

    @Override
    public CartDto getCart(long user_id) {

        CartDto cart = getActiveCart(user_id);
        log.info("cart id in getCart-> {}" , cart.getId() );

        List<CartProduct> cartProducts = List.of();
        Long cartId = cart.getId();

        SqlParameterSource params = new MapSqlParameterSource().addValue("cartId",cartId);
        try{
           cartProducts = jdbc.query(SELECT_ITEMS_FOR_CART_QUERY, params, new CartProductRowMapper());
        }catch(BadSqlGrammarException bex){
            log.error("Sql grammar exception. cause{}", Objects.requireNonNull(bex.getSQLException()).getMessage(), bex  );
        } catch (DataAccessException dae) {
            log.error(dae.getMessage());
            throw dae;
        }
        cart.setCartProducts(cartProducts);
        return cart;




    }

    //search and get ActiveCart ...if not exist will be created
    @Transactional
    private CartDto getActiveCart(long userId) {
        log.info("User id -> {}" , userId );
        CartDto cart = new CartDto();
        Map<String ,Object> param = Map.of("userId",userId);

        //cart exists
        try{
          cart.setId(jdbc.queryForObject(SELECT_CART_QUERY,param,Long.class));
          return cart;
        }catch(EmptyResultDataAccessException ex){
           try{
               KeyHolder kh = new GeneratedKeyHolder();
               jdbc.update(INSERT_CART_QUERY,new MapSqlParameterSource("userId",userId),kh);

               if(kh.getKey() == null){
                log.error("Generated Key is Null");
                throw new IllegalStateException("NO Generated key was returned  ");
               }

               return new CartDto(kh.getKey().longValue());


              }catch ( DataIntegrityViolationException race){
               cart.setId(jdbc.queryForObject(SELECT_CART_QUERY,param,Long.class));
               return cart;

              }
        }
    }

    @Override
    public CartDto updateCart(long userId, CartProduct cartProduct) {
        //get or create cart
        CartDto  cart = getCart(userId);
        log.info("cart id -> {}" , cart.getId() );
        List<CartProduct> cartProducts = cart.getCartProducts();

        Optional<CartProduct> existing = cartProducts.stream()
              .filter(cp -> cp.getProductId() == cartProduct.getProductId()).findFirst();


      if(existing.isPresent()){
          CartProduct cp = existing.get();
          cp.setQuantity(cp.getQuantity() + cartProduct.getQuantity());
          MapSqlParameterSource param = new MapSqlParameterSource()
                  .addValue("cartId",cart.getId())
                  .addValue("productId",cp.getProductId())
                  .addValue("qty",cp.getQuantity());

              jdbc.update(UPDATE_ITEM_QTY_QUERY,param);
              return getCart(userId);


      }else{
          cartProducts.add(cartProduct);
          KeyHolder kh = new GeneratedKeyHolder();
          MapSqlParameterSource ins = new MapSqlParameterSource()
                  .addValue("cartId",cart.getId())
                  .addValue("productId",cartProduct.getProductId())
                  .addValue("qty",cartProduct.getQuantity());
              jdbc.update(INSERT_ITEM_CART_QUERY,ins,kh);
              Number key = kh.getKey();
              if(key == null){
                  log.error("Generated Key for new item in cart is Null");
                  throw new IllegalStateException("NO Generated key was returned for new item  ");
              }
              return getCart(userId);
      }
    }

    @Override
    public CartDto createCart(long userId, CartProduct cartProduct) {
        KeyHolder kh = new GeneratedKeyHolder();
        SqlParameterSource cartParam = new MapSqlParameterSource().addValue("userId",userId);
        try{
             jdbc.update(INSERT_CART_QUERY,cartParam,kh);

        }catch(DataAccessException dae){
            log.error(dae.getMessage());
            throw dae;
        }
        long cartId = Objects.requireNonNull(kh.getKey()).longValue();
        KeyHolder ikh = new GeneratedKeyHolder();

        SqlParameterSource param = getSqlParam(cartId, userId, cartProduct);
        try{
            jdbc.update(INSERT_ITEM_TO_CART_QUERY,param,ikh);
        }catch (DataAccessException dae) {
            log.error(dae.getMessage());
            throw dae;
        }
        return getCart(userId);
    }

    @Override
    public CartDto deleteCartProduct(long userId, long productId) {

        long cartId = getActiveCart(userId).getId();
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("productId",productId).addValue("cartId",cartId);

        try{
            jdbc.update(DELETE_PRODUCT_QUERY,params);

        }catch (DataAccessException dae){
            log.error("error deleting product fron cart", dae);
            throw dae;

        }

        return getCart(userId);
    }

    @Override
    public CartDto decrementQuantity(long userId, long productId) {

        long cartId = getActiveCart(userId).getId();

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("productId",productId).addValue("cartId",cartId);

        Integer quantity =  getQuantity(params);

       if (quantity == null) {
            return getCart(userId);
        }

        if(quantity <= 1){
          return  deleteCartProduct(userId,productId);
        }

        try{
            jdbc.update(DECREMENT_QUANTITY_QUERY,params);
        } catch (DataAccessException dae) {
            log.error("error deleting product fron cart", dae);
            throw dae;
        }

        return getCart(userId);
    }

    @Override
    public CartDto mergeCart(long userId, CartProduct[] guestCart) {

        if(guestCart == null || guestCart.length == 0){
            return getCart(userId);
        }


        long activeCartId = getActiveCart(userId).getId();
        log.info("cartId => {}" ,activeCartId);

        for(CartProduct cp : guestCart){
            if(cp == null) continue;

            long productId = cp.getProductId();
            if(productId <= 0) continue;

            int qty = cp.getQuantity();
            if(qty <= 0) continue;

           upsertCartProduct(activeCartId, productId,qty);
        }




      return getCart(userId);

    }

    private void upsertCartProduct(long activeCartId, long productId, int qty) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("cartId",activeCartId)
                .addValue("productId" , productId)
                .addValue("qty",qty);
try{
    jdbc.update(UPSERT_CART_PRODUCT,param);
}catch (BadSqlGrammarException ex){
    log.error(Objects.requireNonNull(ex.getSQLException()).toString());
    log.error(ex.getMessage());

}
    }

    private Integer getQuantity(MapSqlParameterSource params) {

        try{
           return jdbc.queryForObject(SELECT_QUANTITY_QUERY, params, Integer.class);

        }catch(EmptyResultDataAccessException empty){
            log.error("Quantity in null", empty);
            throw empty;
        }catch(DataAccessException dae){
            log.error("Can't retrieve quantity", dae);
            throw dae;
        }
    }


    private SqlParameterSource getSqlParam(long cartId, Long userId, CartProduct cartProduct) {

        return new MapSqlParameterSource()
                .addValue("cartId",cartId)
                .addValue("userId",userId)
                .addValue("productId",cartProduct.getProductId())
                .addValue("qty",cartProduct.getQuantity());

    }
}
