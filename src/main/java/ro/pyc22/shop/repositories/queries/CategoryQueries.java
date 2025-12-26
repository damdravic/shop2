package ro.pyc22.shop.repositories.queries;

public class CategoryQueries {

    public static final String INSERT_CATEGORY_QUERY ="INSERT INTO categories (parent_id, category_name, category_description, slug, is_active, sort_order) VALUES " +
            "(:parentId, :categoryName, :categoryDescription, :slug, :isActive, :sortOrder) ";
    public static final String SELECT_ALL_CATEGORIES_QUERY ="SELECT * FROM categories ";



}
