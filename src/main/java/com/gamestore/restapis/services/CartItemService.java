package com.gamestore.restapis.services;

import com.gamestore.restapis.dtos.CartItemDto;
import com.gamestore.restapis.entities.CartItem;
import com.gamestore.restapis.entities.Product;
import com.gamestore.restapis.entities.User;
import com.gamestore.restapis.repositories.CartItemRepository;
import com.gamestore.restapis.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CartItemDto> getCart(User user) {
        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public CartItemDto addToCart(User user, Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        var cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .map(item -> {
                    item.setQuantity(Math.min(item.getQuantity() + 1, 99));
                    return item;
                })
                .orElseGet(() -> createCartItem(user, product));

        return toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    public CartItemDto updateQuantity(User user, Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
        }

        var cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));

        cartItem.setQuantity(quantity);
        return toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    public void removeFromCart(User user, Long productId) {
        if (!cartItemRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found");
        }

        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUserId(user.getId());
    }

    private CartItem createCartItem(User user, Product product) {
        var cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        return cartItem;
    }

    private CartItemDto toDto(CartItem cartItem) {
        var product = cartItem.getProduct();
        var dto = new CartItemDto();

        dto.setId(cartItem.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductDescription(product.getDescription());
        dto.setProductPrice(product.getPrice());
        dto.setProductImageUrl(product.getImageUrl());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setQuantity(cartItem.getQuantity());
        dto.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        return dto;
    }
}
