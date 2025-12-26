package ro.pyc22.shop.repositories.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ro.pyc22.shop.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Category.builder()
                .id(rs.getLong("id"))
                .parentId(rs.getLong("parent_id"))
                .categoryName(rs.getString("category_name"))
                .categoryDescription(rs.getString("category_description"))
                .slug(rs.getString("slug"))
                .isActive(rs.getBoolean("is_active"))
                .sortOrder(rs.getInt("sort_order"))
                .build();
    }
}
