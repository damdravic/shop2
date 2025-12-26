package ro.pyc22.shop.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Category {

    private Long id;
    private Long parentId;
    private String categoryName;
    private String categoryDescription;
    private String slug;
    private Boolean isActive;
    private Integer sortOrder;

}
