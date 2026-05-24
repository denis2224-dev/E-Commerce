import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from './products.service';

@Injectable({ providedIn: 'root' })
export class WishlistService {
  constructor(private http: HttpClient) {}

  getWishlist() {
    return this.http.get<Product[]>(this.wishlistUrl);
  }

  addToWishlist(productId: number) {
    return this.http.post<Product>(`${this.wishlistUrl}/${productId}`, null);
  }

  removeFromWishlist(productId: number) {
    return this.http.delete<void>(`${this.wishlistUrl}/${productId}`);
  }

  private readonly wishlistUrl = '/api/me/wishlist';
}
