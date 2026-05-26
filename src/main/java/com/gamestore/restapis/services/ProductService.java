package com.gamestore.restapis.services;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.mappers.ProductMapper;
import com.gamestore.restapis.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(value = "products", key = "#root.args[0] == null ? 'all' : #root.args[0]")
    public List<ProductDto> getProducts(Byte categoryId) {
        if (categoryId == null) {
            return new ArrayList<>(productRepository.findAllWithCategory()
                    .stream()
                    .map(productMapper::toDto)
                    .toList());
        }

        return new ArrayList<>(productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toDto)
                .toList());
    }

    @Cacheable(value = "product", key = "#root.args[0]", unless = "#result == null")
    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElse(null);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        var product = productMapper.toEntity(productDto);
        productRepository.save(product);

        return productMapper.toDto(product);
    }
}
