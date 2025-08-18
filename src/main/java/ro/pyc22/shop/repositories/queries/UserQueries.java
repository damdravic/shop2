package ro.pyc22.shop.repositories.queries;

public class UserQueries {

    public static final String INSERT_USER_QUERY = " INSERT INTO users (firstname,lastname,email,password) VALUES (:firstName, :lastName, :email, :password)";

    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = :email";
}
