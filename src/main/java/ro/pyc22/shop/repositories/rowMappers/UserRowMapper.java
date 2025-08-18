package ro.pyc22.shop.repositories.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ro.pyc22.shop.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

    return  User.builder()
             .id(rs.getLong("id"))
             .firstName(rs.getString("firstname"))
             .lastName(rs.getString("lastname"))
             .email(rs.getString("email"))
             .password(rs.getString("password"))
            .phone(rs.getString("phone"))
            .title(rs.getString("title"))
            .bio(rs.getString("bio"))
            .imageUrl(rs.getString("image_url"))
            .enabled(rs.getBoolean("enabled"))
            .isNotLocked(rs.getBoolean("is_not_locked"))
            .isUsingMfa(rs.getBoolean("is_using_mfa"))
            .createdAt(rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null)
            .build();



    }
}
