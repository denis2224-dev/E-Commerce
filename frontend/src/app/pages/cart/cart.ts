import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  imports: [RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.scss',
})
export class Cart implements OnInit {
  private cartService = inject(CartService);

  isLoadingCart = signal(true);

  cartItems = this.cartService.items;
  cartCount = this.cartService.itemCount;

  cartTotal = computed(() =>
    this.cartItems().reduce((total, item) => total + item.subtotal, 0)
  );

  ngOnInit() {
    this.loadCart();
  }

  increaseQuantity(productId: number) {
    const quantity = this.cartService.getQuantity(productId);
    this.cartService.updateQuantity(productId, quantity + 1).subscribe({
      error: () => {
        console.log('Could not increase cart quantity');
      },
    });
  }

  decreaseQuantity(productId: number) {
    const quantity = this.cartService.getQuantity(productId);

    if (quantity <= 1) {
      this.removeFromCart(productId);
      return;
    }

    this.cartService.updateQuantity(productId, quantity - 1).subscribe({
      error: () => {
        console.log('Could not decrease cart quantity');
      },
    });
  }

  updateQuantity(productId: number, event: Event) {
    const input = event.target as HTMLInputElement;
    const quantity = input.valueAsNumber;

    if (!Number.isFinite(quantity)) {
      return;
    }

    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }

    this.cartService.updateQuantity(productId, quantity).subscribe({
      error: () => {
        console.log('Could not update cart quantity');
      },
    });
  }

  removeFromCart(productId: number) {
    this.cartService.removeProduct(productId).subscribe({
      error: () => {
        console.log('Could not remove product from cart');
      },
    });
  }

  clearCart() {
    this.cartService.clearCart().subscribe({
      error: () => {
        console.log('Could not clear cart');
      },
    });
  }

  private loadCart() {
    this.cartService.loadCart().subscribe({
      next: () => {
        this.isLoadingCart.set(false);
      },
      error: () => {
        console.log('Could not load cart');
        this.isLoadingCart.set(false);
      },
    });
  }
}
