import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ProductsService, Product } from '../../services/products.service';
import { WishlistService } from '../../services/wishlist.service';

@Component({
  selector: 'app-products',
  imports: [],
  templateUrl: './products.html',
  styleUrl: './products.scss',
})
export class Products implements OnInit {
  products = signal<Product[]>([]);
  wishlistedProductIds = signal<Set<number>>(new Set());
  wishlistRequestIds = signal<Set<number>>(new Set());

  constructor(
    private authService: AuthService,
    private productsService: ProductsService,
    private router: Router,
    private wishlistService: WishlistService,
  ) {}

  ngOnInit() {
    this.loadProducts();

    if (this.authService.isLoggedIn()) {
      this.loadWishlist();
    }
  }

  isWishlisted(productId: number) {
    return this.wishlistedProductIds().has(productId);
  }

  isWishlistUpdating(productId: number) {
    return this.wishlistRequestIds().has(productId);
  }

  toggleWishlist(product: Product) {
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/sign-in');
      return;
    }

    if (this.isWishlistUpdating(product.id)) {
      return;
    }

    this.setWishlistUpdating(product.id, true);

    if (this.isWishlisted(product.id)) {
      this.wishlistService.removeFromWishlist(product.id).subscribe({
        next: () => {
          this.removeWishlistedProduct(product.id);
        },
        error: () => {
          console.log('Could not remove product from wishlist');
          this.setWishlistUpdating(product.id, false);
        },
        complete: () => {
          this.setWishlistUpdating(product.id, false);
        },
      });
      return;
    }

    this.wishlistService.addToWishlist(product.id).subscribe({
      next: () => {
        this.addWishlistedProduct(product.id);
      },
      error: () => {
        console.log('Could not add product to wishlist');
        this.setWishlistUpdating(product.id, false);
      },
      complete: () => {
        this.setWishlistUpdating(product.id, false);
      },
    });
  }

  private loadProducts() {
    this.productsService.getProducts().subscribe({
      next: (products) => {
        this.products.set(products);
      },
      error: () => {
        console.log('Could not load products');
      },
    });
  }

  private loadWishlist() {
    this.wishlistService.getWishlist().subscribe({
      next: (products) => {
        this.wishlistedProductIds.set(new Set(products.map((product) => product.id)));
      },
      error: () => {
        console.log('Could not load wishlist');
      },
    });
  }

  private addWishlistedProduct(productId: number) {
    this.wishlistedProductIds.update((productIds) => {
      const nextProductIds = new Set(productIds);
      nextProductIds.add(productId);
      return nextProductIds;
    });
  }

  private removeWishlistedProduct(productId: number) {
    this.wishlistedProductIds.update((productIds) => {
      const nextProductIds = new Set(productIds);
      nextProductIds.delete(productId);
      return nextProductIds;
    });
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
