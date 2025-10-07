package ro.pyc22.shop.repositories.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ro.pyc22.shop.model.ProductImage;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductImageRowMapper implements RowMapper<ProductImage> {

    @Override
    public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductImage(
               rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getString("image_path")
                );

    }
}
