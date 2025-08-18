package ro.pyc22.shop.model;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private int price;
}
