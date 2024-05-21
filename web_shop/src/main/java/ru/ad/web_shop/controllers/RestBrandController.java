package ru.ad.web_shop.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ad.web_shop.models.BrandDto;
import ru.ad.web_shop.models.BrandToLinkDto;
import ru.ad.web_shop.services.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class RestBrandController {
    private final BrandService service;

    @Operation(summary = "Получение всех брэндов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Брэнды не найдены")
    })
    @GetMapping
    public List<BrandToLinkDto> getAll() {
        return service.getAll().stream().map(service::convertToLink).toList();
    }

    @Operation(summary = "Получение брэнда по айди")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Брэнд не найден")
    })
    @GetMapping("{id}")
    public BrandDto getById(@PathVariable Integer id) {
        return service.convert(service.getById(id));
    }

    @Operation(summary = "Добавление брэнда")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Брэнд создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public BrandToLinkDto create(@RequestBody BrandToLinkDto brand) {
        return service.convertToLink(service.save(brand));
    }

    @Operation(summary = "Изменение брэнда")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Брэнд обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Брэнд не найден")
    })
    @PutMapping
    public BrandDto update(@RequestBody BrandToLinkDto brand) {
        return service.convert(service.update(brand));
    }

    @Operation(summary = "Удаление брэнда")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Брэнд удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Брэнд не найден")
    })
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}