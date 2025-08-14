package org.example.remotly_ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.remotly_ecommerce.model.Category;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private String description;
    private Double mrpPrice;
    private Double sellingPrice;
    private Double discountPercentage;
    private List<String> images ;
    private Category category;
    private String size;
    private String color;


}
