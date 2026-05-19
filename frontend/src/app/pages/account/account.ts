import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Product } from '../../services/products.service';
import { WishlistService } from '../../services/wishlist.service';

@Component({
  selector: 'app-account',
  imports: [RouterLink],
  templateUrl: './account.html',
  styleUrl: './account.scss',
})
export class Account implements OnInit {
  private readonly currentUserId = 1;

  profile = {
    name: 'Denis Moroz',
    email: 'denis@example.com',
    memberSince: 'May 2026',
    level: 'Player',
  };

  libraryGames = [
    { title: 'Ghost Protocol 8', meta: 'Simulation', status: 'Owned' },
    { title: 'Neon Strike 1', meta: 'Action', status: 'Installed' },
    { title: 'Solar Drift 69', meta: 'Adventure', status: 'Owned' },
  ];

  orders = [
    { id: 'GS-1042', date: 'May 18, 2026', total: '$69.99', status: 'Completed' },
    { id: 'GS-1037', date: 'May 10, 2026', total: '$39.99', status: 'Completed' },
    { id: 'GS-1024', date: 'Apr 28, 2026', total: '$19.99', status: 'Completed' },
  ];

  wishlistItems = signal<Product[]>([]);

  stats = computed(() => [
    { label: 'Games', value: String(this.libraryGames.length) },
    { label: 'Wishlist', value: String(this.wishlistItems().length) },
    { label: 'Orders', value: String(this.orders.length) },
  ]);

  constructor(private wishlistService: WishlistService) {}

  ngOnInit() {
    this.loadWishlist();
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
}
