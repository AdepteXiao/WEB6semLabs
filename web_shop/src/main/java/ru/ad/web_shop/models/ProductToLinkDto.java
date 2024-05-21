package ru.ad.web_shop.models;

import lombok.Data;

@Data
public class ProductToLinkDto {
    private Integer id;
    private String name;
    private Float price;
}
