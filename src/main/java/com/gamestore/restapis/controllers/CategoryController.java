package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.CategoryDto;
import com.gamestore.restapis.mappers.CategoryMapper;
import com.gamestore.restapis.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public Iterable<CategoryDto> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Byte id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto,
            UriComponentsBuilder uriBuilder
    ) {
        var category = categoryMapper.toEntity(categoryDto);
        categoryRepository.save(category);

        var savedCategoryDto = categoryMapper.toDto(category);
        var uri = uriBuilder.path("/categories/{id}").buildAndExpand(savedCategoryDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedCategoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Byte id,
            @RequestBody CategoryDto categoryDto
    ) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        categoryMapper.update(categoryDto, category);
        categoryRepository.save(category);

        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Byte id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
