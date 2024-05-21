package ru.ad.web_shop.models;

import lombok.Data;

import java.util.List;

@Data
public class BrandDto {
    private Integer id;
    private String name;
    private List<ProductToLinkDto> products;
}
