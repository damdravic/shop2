package ro.pyc22.shop.model.modelDTO;

import lombok.*;
import ro.pyc22.shop.model.CartProduct;

import java.util.List;

@AllArgsConstructor
@Data
@Setter
@Getter
@NoArgsConstructor
public class CartDto {
   private Long id;
   private  List<CartProduct> cartProducts;

    public CartDto(long id) {
       this.id  = id;
    }
}
