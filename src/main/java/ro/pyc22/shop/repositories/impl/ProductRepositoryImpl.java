package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.Product;
import ro.pyc22.shop.model.ProductImage;
import ro.pyc22.shop.repositories.ProductRepository;
import ro.pyc22.shop.repositories.rowMappers.ImagePathRowMapper;
import ro.pyc22.shop.repositories.rowMappers.ProductImageRowMapper;
import ro.pyc22.shop.repositories.rowMappers.ProductRowMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static ro.pyc22.shop.repositories.queries.ProductQueries.*;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository<Product> {

 private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Product> getAllProducts() {

        try{
           return  jdbc.query(SELECT_ALL_PRODUCTS_QUERY,new ProductRowMapper());

        }catch(DataAccessException dae){
            throw new ApiException(dae.getMessage());
        }

    }

    @Override
    public List<String> getImagesPathsByProductId(Long id) {
        try {
            return jdbc.query(SELECT_ALL_IMAGES_PATHS_BY_PRODUCT_ID_QUERY, Map.of("id",id), new ImagePathRowMapper());
        }catch (DataAccessException dae){
            throw new ApiException(dae.getMessage());
        }
    }


    @Override
    public String savePath(Long id, String path) {


        jdbc.update(INSERT_PATH_QUERY,Map.of("productId",id,"path",path));

        return path;
    }

    @Override
    public List<ProductImage> findImagesForProductIds(List<Long> productsIds) {
        if(productsIds.isEmpty()) return List.of();
        Map<String,Object> params = Map.of("productsIds", productsIds);
             try {
               return   jdbc.query(SELECT_PRODUCTS_IMAGES_BY_IDS_QUERY, params,new  ProductImageRowMapper());
             }catch(DataAccessException ex){
                 throw new ApiException("In findImagesForProductIds ->" + ex.getMessage());

        }

    }

    @Override
    public Product createProduct(Product product) {

        KeyHolder kh = new GeneratedKeyHolder();
        SqlParameterSource params = getProdParams(product);
        try{
            jdbc.update(INSERT_PRODUCT_QUERY,params,kh);
        }catch (DataAccessException dae){
            //TODO implement real exceptionHandle
            System.out.println("DataAccessException: " + dae.getMessage());

        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        product.setId(kh.getKey().longValue());
        return product;
    }

    private SqlParameterSource getProdParams(Product product) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name",product.getName())
                .addValue("productCode",product.getProductCode())
                .addValue("internProdCode",product.getInternProdCode())
                .addValue("qrCodeData",product.getQrCodeData())
                .addValue("description",product.getDescription())
                .addValue("mainImage",product.getMainImage())
                .addValue("price",product.getPrice())
                .addValue("costPrice",product.getCostPrice())
                .addValue("markupPercentage",getMarkupPercentage(product.getPrice(),product.getCostPrice()))
                .addValue("stock",product.getStock())
                .addValue("isAvailable",product.isAvailable())
                .addValue("warranty",product.getWarrantyMonths())
                .addValue("vat",product.getVatPercentage());


        return params;

    }


    //Todo  --  it s not calculating real
    private BigDecimal getMarkupPercentage(BigDecimal price, BigDecimal costPrice) {
        return BigDecimal.valueOf(1);
    }

    @Override
    public Product getProductById(Long id) {
        try {
            return jdbc.queryForObject(SELECT_PRODUCT_BY_ID_QUERY, Map.of("id", id), new ProductRowMapper());
        }catch( DataAccessException dae){
            System.out.println("DataAccessException : " + dae.getMessage());
            throw new ApiException(dae.getMessage());
        }catch (Exception ex){
            throw new ApiException(ex.getMessage());

        }

    }
}
