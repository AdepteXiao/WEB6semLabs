package ru.ad.web_shop.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ad.web_shop.models.CategoryDto;
import ru.ad.web_shop.models.CategoryToLinkDto;
import ru.ad.web_shop.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class RestCategoryController {
    private final CategoryService service;

    @Operation(summary = "Получение всех категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Категории не найдены")
    })
    @GetMapping
    public List<CategoryToLinkDto> getAll() {
        return service.getAll().stream().map(service::convertToLink).toList();
    }

    @Operation(summary = "Получение категории по айди")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("{id}")
    public CategoryDto getById(@PathVariable Integer id) {
        return service.convert(service.getById(id));
    }

    @Operation(summary = "Добавление категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категория создана"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public CategoryToLinkDto create(@RequestBody CategoryToLinkDto category) {
        return service.convertToLink(service.save(category));
    }

    @Operation(summary = "Изменение категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @PutMapping
    public CategoryDto update(@RequestBody CategoryToLinkDto category) {
        return service.convert(service.update(category));
    }

    @Operation(summary = "Удаление категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория удалена"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
