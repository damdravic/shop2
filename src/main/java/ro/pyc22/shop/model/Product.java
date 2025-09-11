package ro.pyc22.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private String productCode;
    private String internProdCode;
    private String qrCodeData;
    private String description;
    private String mainImage;
    private BigDecimal price;
    private BigDecimal costPrice;
    private BigDecimal markupPercentage;
    private int stock;
    private boolean isAvailable;
    private int warrantyMonths;
    private int vatPercentage;

}