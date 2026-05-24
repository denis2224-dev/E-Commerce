package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ProductDto;
import com.gamestore.restapis.entities.WishlistItem;
import com.gamestore.restapis.mappers.ProductMapper;
import com.gamestore.restapis.repositories.ProductRepository;
import com.gamestore.restapis.repositories.WishlistItemRepository;
import com.gamestore.restapis.services.CurrentUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me/wishlist")
public class WishlistItemController {
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getWishlist(@AuthenticationPrincipal Jwt jwt) {
        var user = currentUserService.getCurrentUser(jwt);

        var products = wishlistItemRepository.findByUserId(user.getId())
                .stream()
                .map(WishlistItem::getProduct)
                .map(productMapper::toDto)
                .toList();

        return ResponseEntity.ok(products);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ProductDto> addToWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal Jwt jwt,
            UriComponentsBuilder uriBuilder
    ) {
        var user = currentUserService.getCurrentUser(jwt);
        var product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (wishlistItemRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }

        var wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .build();

        wishlistItemRepository.save(wishlistItem);

        var uri = uriBuilder
                .path("/api/me/wishlist/{productId}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var user = currentUserService.getCurrentUser(jwt);

        if (!wishlistItemRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            return ResponseEntity.notFound().build();
        }

        wishlistItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
