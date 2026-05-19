import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from './products.service';

@Injectable({ providedIn: 'root' })
export class WishlistService {
  constructor(private http: HttpClient) {}

  getWishlist(userId: number) {
    return this.http.get<Product[]>(this.wishlistUrl(userId));
  }

  addToWishlist(userId: number, productId: number) {
    return this.http.post<Product>(`${this.wishlistUrl(userId)}/${productId}`, null);
  }

  removeFromWishlist(userId: number, productId: number) {
    return this.http.delete<void>(`${this.wishlistUrl(userId)}/${productId}`);
  }

  private wishlistUrl(userId: number) {
    return `/api/users/${userId}/wishlist`;
  }
}
