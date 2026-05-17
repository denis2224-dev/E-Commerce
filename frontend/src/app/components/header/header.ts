import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  brandName = 'E-Commerce';

  navItems = [
    { label: 'Products', path: '/products' },
    { label: 'Categories', path: '/categories' },
    { label: 'Deals', path: '/deals' },
    { label: 'Cart', path: '/cart' },
    { label: 'Account', path: '/account' },
  ];
}
