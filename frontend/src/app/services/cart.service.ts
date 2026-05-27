import { HttpClient } from '@angular/common/http';
import { computed, Injectable, signal } from '@angular/core';
import { tap } from 'rxjs';

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  productDescription: string;
  productPrice: number;
  productImageUrl: string | null;
  categoryId: number | null;
  quantity: number;
  subtotal: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly cartUrl = '/api/me/cart';

  items = signal<CartItem[]>([]);

  itemCount = computed(() =>
    this.items().reduce((total, item) => total + item.quantity, 0)
  );

  constructor(private http: HttpClient) {}

  loadCart() {
    return this.http.get<CartItem[]>(this.cartUrl).pipe(
      tap((items) => {
        this.items.set(items);
      })
    );
  }

  addProduct(productId: number) {
    return this.http.post<CartItem>(`${this.cartUrl}/items/${productId}`, null).pipe(
      tap((item) => {
        this.upsertCartItem(item);
      })
    );
  }

  updateQuantity(productId: number, quantity: number) {
    return this.http
      .patch<CartItem>(`${this.cartUrl}/items/${productId}`, { quantity })
      .pipe(
        tap((item) => {
          this.upsertCartItem(item);
        })
      );
  }

  removeProduct(productId: number) {
    return this.http.delete<void>(`${this.cartUrl}/items/${productId}`).pipe(
      tap(() => {
        this.items.update((items) => items.filter((item) => item.productId !== productId));
      })
    );
  }

  clearCart() {
    return this.http.delete<void>(this.cartUrl).pipe(
      tap(() => {
        this.items.set([]);
      })
    );
  }

  resetCartState() {
    this.items.set([]);
  }

  isInCart(productId: number) {
    return this.items().some((item) => item.productId === productId);
  }

  getQuantity(productId: number) {
    return this.items().find((item) => item.productId === productId)?.quantity ?? 0;
  }

  private upsertCartItem(cartItem: CartItem) {
    this.items.update((items) => {
      const itemExists = items.some((item) => item.productId === cartItem.productId);

      if (!itemExists) {
        return [...items, cartItem];
      }

      return items.map((item) =>
        item.productId === cartItem.productId ? cartItem : item
      );
    });
  }
}
