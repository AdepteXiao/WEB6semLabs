package ru.ad.web_shop.models;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private BrandToLinkDto brand;
    private CategoryToLinkDto category;
}
