package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.repositories.ProductRepository;
import ro.pyc22.shop.repositories.rowMappers.ProductRowMapper;

import java.util.List;

import static ro.pyc22.shop.repositories.queries.ProductQueries.SELECT_ALL_PRODUCTS_QUERY;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository<Product> {

 private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Product> getAllProducts() {

        try{
            jdbc.query(SELECT_ALL_PRODUCTS_QUERY,new ProductRowMapper());
            return null;
        }catch(DataAccessException dae){
            throw new ApiException(dae.getMessage());
        }

    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }
}
