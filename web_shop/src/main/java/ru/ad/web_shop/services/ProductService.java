package ru.ad.web_shop.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ru.ad.web_shop.entities.Product;
import ru.ad.web_shop.models.BrandToLinkDto;
import ru.ad.web_shop.models.CategoryToLinkDto;
import ru.ad.web_shop.models.ProductDto;
import ru.ad.web_shop.models.ProductUpdateDto;
import ru.ad.web_shop.repositories.BrandRepository;
import ru.ad.web_shop.repositories.CategoryRepository;
import ru.ad.web_shop.repositories.ProductRepository;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mm;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByBrandId(Integer brandId, Model model) {
        var brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("No brand with id=" + brandId));
        model.addAttribute("brand", mm.map(brand, BrandToLinkDto.class));
        return productRepository.findAllByBrand(brandId);
    }

    public List<Product> getByCategoryId(Integer categoryId, Model model) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("No category with id=" + categoryId));
        model.addAttribute("brand", mm.map(category, CategoryToLinkDto.class));
        return productRepository.findAllByCategory(categoryId);
    }


    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No product with id" + id));
    }


    public ProductDto convert(Product product) {
        var dto = new ProductDto();
        dto.setBrand(mm.map(product.getBrand(), BrandToLinkDto.class));
        dto.setCategory(mm.map(product.getCategory(), CategoryToLinkDto.class));
        mm.map(product, dto);
        return dto;
    }


    @Transactional
    public Product save(ProductUpdateDto productUpdateDto) {
        Product product = new Product();
        product.setBrand(brandRepository.findById(productUpdateDto.getBrandId())
                .orElseThrow(() -> new RuntimeException("No brand with id=" + productUpdateDto.getBrandId())));
        product.setCategory(categoryRepository.findById(productUpdateDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("No category with id=" + productUpdateDto.getCategoryId())));
        mm.map(productUpdateDto, product);
        return productRepository.save(product);
    }

    @Transactional
    public Product update(ProductUpdateDto productUpdateDto, Integer id) {
        var exProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product with id = " + id + " not found"));

        Product product = new Product();
        BeanUtils.copyProperties(productUpdateDto, product, getNullPropertyNames(productUpdateDto));

        if (productUpdateDto.getBrandId() != null) {
            product.setBrand(brandRepository.findById(productUpdateDto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("No brand with id=" + productUpdateDto.getBrandId())));
        }
        if (productUpdateDto.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(productUpdateDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("No category with id=" + productUpdateDto.getCategoryId())));
        }

        BeanUtils.copyProperties(product, exProduct, getNullPropertyNames(product));

        return productRepository.save(exProduct);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    public void delete(Integer id) {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product with id = " + id + " not found");
        }
    }
}
