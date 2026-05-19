package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.entities.WishlistItem;
import com.gamestore.restapis.mappers.ProductMapper;
import com.gamestore.restapis.repositories.ProductRepository;
import com.gamestore.restapis.repositories.UserRepository;
import com.gamestore.restapis.repositories.WishlistItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users/{userId}/wishlist")
public class WishlistItemController {
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getWishlist(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        var products = wishlistItemRepository.findByUserId(userId)
                .stream()
                .map(WishlistItem::getProduct)
                .map(productMapper::toDto)
                .toList();

        return ResponseEntity.ok(products);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ProductDto> addToWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId,
            UriComponentsBuilder uriBuilder
    ) {
        var user = userRepository.findById(userId).orElse(null);
        var product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return ResponseEntity.notFound().build();
        }

        if (wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }

        var wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .build();

        wishlistItemRepository.save(wishlistItem);

        var uri = uriBuilder
                .path("/api/users/{userId}/wishlist/{productId}")
                .buildAndExpand(userId, productId)
                .toUri();

        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        if (!wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            return ResponseEntity.notFound().build();
        }

        wishlistItemRepository.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
