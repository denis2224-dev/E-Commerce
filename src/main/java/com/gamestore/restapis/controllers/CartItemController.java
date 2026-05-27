package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.CartItemDto;
import com.gamestore.restapis.dtos.UpdateCartItemRequest;
import com.gamestore.restapis.services.CartItemService;
import com.gamestore.restapis.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me/cart")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart(@AuthenticationPrincipal Jwt jwt) {
        var user = currentUserService.getCurrentUser(jwt);
        return ResponseEntity.ok(cartItemService.getCart(user));
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal Jwt jwt,
            UriComponentsBuilder uriBuilder
    ) {
        var user = currentUserService.getCurrentUser(jwt);
        var cartItem = cartItemService.addToCart(user, productId);

        var uri = uriBuilder
                .path("/api/me/cart/items/{productId}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(cartItem);
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var user = currentUserService.getCurrentUser(jwt);
        return ResponseEntity.ok(cartItemService.updateQuantity(user, productId, request.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var user = currentUserService.getCurrentUser(jwt);
        cartItemService.removeFromCart(user, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Jwt jwt) {
        var user = currentUserService.getCurrentUser(jwt);
        cartItemService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
