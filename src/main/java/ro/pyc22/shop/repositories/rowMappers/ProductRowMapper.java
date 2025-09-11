package ro.pyc22.shop.repositories.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ro.pyc22.shop.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .productCode(rs.getString("product_code"))
                .internProdCode(rs.getString("intern_prod_code"))
                .qrCodeData(rs.getString("qr_code_data"))
                .description(rs.getString("description"))
                .mainImage(rs.getString("main_image"))
                .price(rs.getBigDecimal("price"))
                .costPrice(rs.getBigDecimal("cost_price"))
                .markupPercentage(rs.getBigDecimal("markup_percentage"))
                .stock(rs.getInt("stock"))
                .isAvailable(rs.getBoolean("is_available"))
                .warrantyMonths(rs.getInt("warranty_months"))
                .vatPercentage(rs.getInt("vat_percentage"))
                .build();

    }
}
