package ru.ad.web_shop.models;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Integer id;
    private String name;
    private List<ProductToLinkDto> products;
}
