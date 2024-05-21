package ru.ad.web_shop.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ad.web_shop.entities.Brand;
import ru.ad.web_shop.entities.Category;
import ru.ad.web_shop.models.*;
import ru.ad.web_shop.repositories.BrandRepository;
import ru.ad.web_shop.repositories.ProductRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mm;


    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public BrandDto convert(Brand brand) {
        var dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setProducts(
                productRepository.findAllByCategory(brand.getId())
                        .stream()
                        .map(
                                product -> mm.map(product, ProductToLinkDto.class)
                        ).toList()
        );
        return dto;
    }

    public BrandToLinkDto convertToLink(Brand brand) {
        return mm.map(brand, BrandToLinkDto.class);
    }

    public Brand getById(Integer id) {
        return brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand with id = " + id + "not found"));
    }

    public Brand save(BrandToLinkDto brand) {
        var newBrand = new Brand();
        newBrand.setName(brand.getName());
        return brandRepository.save(newBrand);
    }

    public Brand update(BrandToLinkDto brand) {
        var exBrand = brandRepository.findById(brand.getId()).orElseThrow(() -> new RuntimeException("Brand with id = " + brand.getId() + "not found"));
        exBrand.setName(brand.getName());
        return brandRepository.save(exBrand);
    }

    public void delete(Integer id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
        } else {
            throw new RuntimeException("Brand with id = " + id + " not found");
        }
    }
}
