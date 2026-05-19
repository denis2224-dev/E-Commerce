import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Product } from '../../services/products.service';
import { WishlistService } from '../../services/wishlist.service';

@Component({
  selector: 'app-wishlist',
  imports: [RouterLink],
  templateUrl: './wishlist.html',
  styleUrl: './wishlist.scss',
})
export class Wishlist implements OnInit {
  private readonly currentUserId = 1;

  wishlistItems = signal<Product[]>([]);
  wishlistRequestIds = signal<Set<number>>(new Set());

  wishlistTotal = computed(() =>
    this.wishlistItems().reduce((total, product) => total + product.price, 0)
  );

  constructor(private wishlistService: WishlistService) {}

  ngOnInit() {
    this.loadWishlist();
  }

  isWishlistUpdating(productId: number) {
    return this.wishlistRequestIds().has(productId);
  }

  removeFromWishlist(product: Product) {
    if (this.isWishlistUpdating(product.id)) {
      return;
    }

    this.setWishlistUpdating(product.id, true);

    this.wishlistService.removeFromWishlist(this.currentUserId, product.id).subscribe({
      next: () => {
        this.removeWishlistItem(product.id);
      },
      error: () => {
        console.log('Could not remove product from wishlist');
        this.setWishlistUpdating(product.id, false);
      },
      complete: () => {
        this.setWishlistUpdating(product.id, false);
      },
    });
  }

  private loadWishlist() {
    this.wishlistService.getWishlist(this.currentUserId).subscribe({
      next: (products) => {
        this.wishlistItems.set(products);
      },
      error: () => {
        console.log('Could not load wishlist');
      },
    });
  }

  private removeWishlistItem(productId: number) {
    this.wishlistItems.update((products) =>
      products.filter((product) => product.id !== productId)
    );
  }

  private setWishlistUpdating(productId: number, isUpdating: boolean) {
    this.wishlistRequestIds.update((productIds) => {
      const nextProductIds = new Set(productIds);

      if (isUpdating) {
        nextProductIds.add(productId);
      } else {
        nextProductIds.delete(productId);
      }

      return nextProductIds;
    });
  }
}
