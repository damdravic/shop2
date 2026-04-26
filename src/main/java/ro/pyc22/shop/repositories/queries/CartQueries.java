package ro.pyc22.shop.repositories.queries;

public class CartQueries {

    public static final String CHECK_CART_QUERY= "SELECT id FROM cart WHERE user_id = :userId AND status = 'ACTIVE' ";
    public static final String INSERT_CART_QUERY =" INSERT INTO cart (user_id) VALUES (:userId) ";
    public static final String INSERT_ITEM_TO_CART_QUERY="INSERT INTO cart_item (cart_id, user_id, product_id, quantity) VALUES (:cartId, :userId, :productId, :qty)";
    public static final String SELECT_CART_QUERY = "SELECT id FROM cart WHERE user_id=:userId AND status = 'ACTIVE' ";
    public static final String SELECT_ITEMS_FOR_CART_QUERY = "SELECT id ,cart_id, product_id, quantity, updated_at from cart_item WHERE cart_id = :cartId ";
    public static final String UPDATE_ITEM_QTY_QUERY = "UPDATE cart_item SET quantity = :qty WHERE cart_id = :cartId AND product_id = :productId";
    public static final String INSERT_ITEM_CART_QUERY = "INSERT INTO cart_item (cart_id, product_id, quantity ) VALUES (:cartId, :productId, :qty ) ";
    public static final String DELETE_PRODUCT_QUERY = "DELETE FROM  cart_item WHERE cart_id = :cartId AND product_id = :productId";
    public static final String DECREMENT_QUANTITY_QUERY = "UPDATE cart_item SET quantity = quantity - 1 WHERE cart_id = :cartId AND product_id = :productId ";
    public static final String SELECT_QUANTITY_QUERY = " SELECT quantity FROM cart_item WHERE cart_id = :cartId AND product_id = :productId";
    //public static final String DELETE_PRODUCT_QUERY= " DELETE ci FROM cart_item ci JOIN cart c On c.id = ci.cart_id WHERE c.user_id = : userId AND ci.product_id = :productId AND c.status = 'ACTIVE'";
    public static final String UPSERT_CART_PRODUCT ="INSERT INTO cart_item (cart_id, product_id, quantity ) VALUES " +
            "(:cartId, :productId, :qty) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
}
