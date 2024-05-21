package ru.ad.web_shop.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ad.web_shop.entities.Category;
import ru.ad.web_shop.models.CategoryDto;
import ru.ad.web_shop.models.CategoryToLinkDto;
import ru.ad.web_shop.models.ProductToLinkDto;
import ru.ad.web_shop.repositories.CategoryRepository;
import ru.ad.web_shop.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mm;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id = " + id + "not found"));
    }

    public CategoryDto convert(Category category) {
        var dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProducts(
                productRepository.findAllByCategory(category.getId())
                        .stream()
                        .map(
                                product -> mm.map(product, ProductToLinkDto.class)
                        ).toList()
        );
        return dto;
    }

    public CategoryToLinkDto convertToLink(Category category) {
        return mm.map(category, CategoryToLinkDto.class);
    }


    @Transactional
    public Category save(CategoryToLinkDto category) {
        var newCategory = new Category();
        newCategory.setName(category.getName());
        return categoryRepository.save(newCategory);
    }

    @Transactional
    public Category update(CategoryToLinkDto category) {
        var exCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new RuntimeException("Category with id = " + category.getId() + "not found"));
        exCategory.setName(category.getName());
        return categoryRepository.save(exCategory);
    }

    @Transactional
    public void delete(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category with id = " + id + " not found");
        }
    }
}
