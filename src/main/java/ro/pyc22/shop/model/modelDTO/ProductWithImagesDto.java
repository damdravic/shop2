package ro.pyc22.shop.model.modelDTO;

import lombok.*;
import ro.pyc22.shop.model.Product;

import java.util.List;
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class ProductWithImagesDto {
    private Product product;
    private List<String> imagesPaths;
}
