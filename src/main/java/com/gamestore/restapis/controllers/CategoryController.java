package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.CategoryDto;
import com.gamestore.restapis.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Iterable<CategoryDto> getAllCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Byte id) {
        var category = categoryService.getCategory(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto,
            UriComponentsBuilder uriBuilder
    ) {
        var savedCategoryDto = categoryService.createCategory(categoryDto);
        var uri = uriBuilder.path("/api/categories/{id}").buildAndExpand(savedCategoryDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedCategoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Byte id,
            @RequestBody CategoryDto categoryDto
    ) {
        var category = categoryService.updateCategory(id, categoryDto);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Byte id) {
        if (!categoryService.deleteCategory(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
