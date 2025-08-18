package ro.pyc22.shop.repositories.queries;

public class RoleQueries {

    public static final String INSERT_ROLE_QUERY ="INSERT INTO roles (name, permission) VALUES (:name, :permission)";
    public static final String SELECT_ROLE_BY_NAME_QUERY = " SELECT * FROM roles WHERE name = :name";
    public static final String INSERT_ROLE_TO_USERS_ROLES = "INSERT INTO users_roles (user_id,role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_USER_ID_QUERY = "SELECT * FROM roles r " +
            "JOIN users_roles ur ON ur.role_id = r.id" +
            "JOIN users u ON u.id = ur.user_id " +
            "WHERE u.id = :id";

    public static final String SELECT_ALL_ROLES_BY_USER_ID = "SELECT DISTINCT r.id, r.name, r.permission " +
    "FROM roles r "+
    "JOIN users_roles ur ON ur.role_id = r.id "+
    "WHERE ur.user_id = :id ";
}
