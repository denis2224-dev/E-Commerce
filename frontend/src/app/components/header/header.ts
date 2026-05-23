import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  brandName = 'Game Store';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  navItems = [
    { label: 'Games', path: '/games' },
    { label: 'Categories', path: '/categories' },
    { label: 'Deals', path: '/deals' },
    { label: 'Wishlist', path: '/wishlist' },
    { label: 'Account', path: '/account' },
  ];

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
    this.router.navigateByUrl('/sign-in');
  }
}
