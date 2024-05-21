package ru.ad.web_shop.entities;

import lombok.Getter;
import ru.ad.web_shop.models.ProductDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShoppingCart {
    private final List<ProductDto> products = new ArrayList<>();

    public void addProduct(ProductDto product) {
        products.add(product);
    }

    public void clearCart() {
        products.clear();
    }

    @Override
    public String toString() {
        return "Cart: " + products.stream()
                .map(ProductDto::getName)
                .collect(Collectors.joining(", "));
    }

    public float getSum() {
        return products.stream()
                .map(ProductDto::getPrice)
                .reduce(0f, Float::sum);
    }

    public void removeProduct(int id) {
        for (ProductDto good : products) {
            if (good.getId() == id) {
                products.remove(good);
                return;
            }
        }
    }
}


