package ro.pyc22.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private Long id;
    private Long product_Id;
    private String path;

}
