package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.repositories.CategoryRepository;
import com.gamestore.restapis.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(required = false, name = "categoryId") Byte categoryId
    ) {
        return productService.getProducts(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productService.getProduct(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
    ) {
        if (productDto.getCategoryId() == null || !categoryRepository.existsById(productDto.getCategoryId())) {
            return ResponseEntity.badRequest().build();
        }

        var savedProductDto = productService.createProduct(productDto);
        var uri = uriBuilder.path("/api/products/{id}").buildAndExpand(savedProductDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedProductDto);
    }
}
