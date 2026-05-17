import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  brandName = 'Game Store';

  navItems = [
    { label: 'Games', path: '/products' },
    { label: 'Categories', path: '/categories' },
    { label: 'Deals', path: '/deals' },
    { label: 'Wishlist', path: '/wishlist' },
    { label: 'Account', path: '/account' },
  ];
}
