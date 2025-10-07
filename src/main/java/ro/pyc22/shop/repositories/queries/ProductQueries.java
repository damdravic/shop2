package ro.pyc22.shop.repositories.queries;

public class ProductQueries {


    public static final String INSERT_PRODUCT_QUERY ="INSERT INTO products " +
            "(name, product_code, intern_prod_code, qr_code_data, description, main_image, price, cost_price, markup_percentage, stock, is_available, warranty_months,vat_percentage)" +
            " VALUES " +
            "(:name, :productCode, :internProdCode, :qrCodeData, :description, :mainImage, :price, :costPrice, :markupPercentage, :stock, :isAvailable, :warranty, :vat) ";
    public static final String SELECT_PRODUCT_BY_ID_QUERY ="SELECT * FROM products " +
            " WHERE id = :id";

    public static final String INSERT_PATH_QUERY = "INSERT INTO images_paths (product_id,image_path) VALUES (:productId, :path)";

    public static final String SELECT_ALL_PRODUCTS_QUERY = "SELECT * FROM products";

    public static final String SELECT_ALL_IMAGES_PATHS_BY_PRODUCT_ID_QUERY = "SELECT * FROM images_paths WHERE product_id = :id";

    public static final String SELECT_PRODUCTS_IMAGES_BY_IDS_QUERY = " SELECT id, product_id, image_path FROM images_paths WHERE product_id IN ( :productsIds)";
}
