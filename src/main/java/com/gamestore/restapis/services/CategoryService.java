package com.gamestore.restapis.services;

import com.gamestore.restapis.dtos.CategoryDto;
import com.gamestore.restapis.mappers.CategoryMapper;
import com.gamestore.restapis.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDto> getCategories() {
        return new ArrayList<>(StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryMapper::toDto)
                .toList());
    }

    @Cacheable(value = "category", key = "#root.args[0]", unless = "#result == null")
    public CategoryDto getCategory(Byte id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElse(null);
    }

    @CacheEvict(value = {"categories", "category", "products", "product"}, allEntries = true)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        var category = categoryMapper.toEntity(categoryDto);
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @CacheEvict(value = {"categories", "category", "products", "product"}, allEntries = true)
    public CategoryDto updateCategory(Byte id, CategoryDto categoryDto) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }

        categoryMapper.update(categoryDto, category);
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @CacheEvict(value = {"categories", "category", "products", "product"}, allEntries = true)
    public boolean deleteCategory(Byte id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }

        categoryRepository.delete(category);
        return true;
    }
}
