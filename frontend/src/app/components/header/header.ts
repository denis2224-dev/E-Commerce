import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit {
  brandName = 'Game Store';

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router,
  ) {}

  navItems = [
    { label: 'Games', path: '/games' },
    { label: 'Wishlist', path: '/wishlist' },
    { label: 'Account', path: '/account' },
  ];

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.cartService.loadCart().subscribe({
        error: () => {
          console.log('Could not load cart count');
        },
      });
    }
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  cartItemCount() {
    return this.cartService.itemCount();
  }

  logout() {
    this.authService.logout();
    this.cartService.resetCartState();
    this.router.navigateByUrl('/sign-in');
  }
}
