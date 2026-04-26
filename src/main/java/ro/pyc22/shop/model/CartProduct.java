package ro.pyc22.shop.model;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CartProduct {
    private long id;
    private long cartId;
    private long productId;
    private int quantity;
    private Date updatedAt;

}
