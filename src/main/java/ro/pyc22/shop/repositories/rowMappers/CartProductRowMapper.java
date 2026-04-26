package ro.pyc22.shop.repositories.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ro.pyc22.shop.model.CartProduct;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartProductRowMapper implements RowMapper<CartProduct> {
    @Override
    public CartProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CartProduct(
                rs.getLong("id"),
                rs.getLong("cart_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getDate("updated_at")
        );
    }
}
