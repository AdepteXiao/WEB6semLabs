package ru.ad.web_shop.models;

import lombok.Data;

@Data
public class ProductUpdateDto {
    private Integer id;
    private String name;
    private Float price;
    private String description;
    private Integer brandId;
    private Integer categoryId;
}
