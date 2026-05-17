import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  categories = [
    { name: 'Consoles', items: 'Next-gen systems', path: '/categories' },
    { name: 'Games', items: 'New releases', path: '/products' },
    { name: 'Accessories', items: 'Controllers, headsets, gear', path: '/categories' },
    { name: 'Gift Cards', items: 'Digital credit', path: '/categories' },
  ];

  featuredProducts = [
    {
      name: 'Pulse Controller',
      category: 'Accessories',
      price: '$69.99',
      tag: 'Best seller',
    },
    {
      name: 'Neon Strike',
      category: 'Games',
      price: '$59.99',
      tag: 'New',
    },
    {
      name: 'Arena Headset',
      category: 'Audio',
      price: '$89.99',
      tag: 'Deal',
    },
  ];

  benefits = ['Fast checkout', 'Secure payments', 'Fresh inventory'];
}
