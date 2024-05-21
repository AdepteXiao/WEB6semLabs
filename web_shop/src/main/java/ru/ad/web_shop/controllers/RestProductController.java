package ru.ad.web_shop.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ad.web_shop.entities.Product;
import ru.ad.web_shop.models.ProductDto;
import ru.ad.web_shop.models.ProductUpdateDto;
import ru.ad.web_shop.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class RestProductController {
    private final ProductService service;

    @Operation(summary = "Получение всех товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Товары не найдены")
    })
    @GetMapping
    public List<ProductDto> getAll() {
        return service.getAll().stream().map(service::convert).toList();
    }

    @Operation(summary = "Получение товара по айди")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Товары не найдены")
    })
    @GetMapping("{id}")
    public ProductDto getById(@PathVariable Integer id) {
        return service.convert(service.getById(id));
    }

    @Operation(summary = "Добавление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
    })
    @PostMapping
    public ProductDto create(@RequestBody ProductUpdateDto product) {
        return service.convert(service.save(product));
    }

    @Operation(summary = "Изменение товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Товары не найдены")
    })
    @PutMapping("{id}")
    public ProductDto update(@PathVariable Integer id, @RequestBody ProductUpdateDto product) {
        return service.convert(service.update(product, id));
    }

    @Operation(summary = "Удаление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Товары не найдены")
    })
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
