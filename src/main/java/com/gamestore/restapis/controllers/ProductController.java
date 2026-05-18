package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.entities.Product;
import com.gamestore.restapis.mappers.ProductMapper;
import com.gamestore.restapis.repositories.CategoryRepository;
import com.gamestore.restapis.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(required = false, name = "categoryId") Byte categoryId
    ) {
        List<Product> products;
        if (categoryId == null) {
            products = productRepository.findAllWithCategory();
        } else {
            products = productRepository.findByCategoryId(categoryId);
        }

        return products
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
    ) {
        if (productDto.getCategoryId() == null || !categoryRepository.existsById(productDto.getCategoryId())) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);
        productRepository.save(product);

        var savedProductDto = productMapper.toDto(product);
        var uri = uriBuilder.path("/api/products/{id}").buildAndExpand(savedProductDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedProductDto);
    }
}
