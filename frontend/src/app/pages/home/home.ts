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
    { name: 'Action', items: 'Fast-paced campaigns', path: '/categories' },
    { name: 'RPG', items: 'Deep worlds and builds', path: '/categories' },
    { name: 'Sports', items: 'Competitive matchups', path: '/categories' },
    { name: 'Strategy', items: 'Tactical decisions', path: '/categories' },
  ];

  featuredGames = [
    {
      name: 'Ghost of Yotei',
      category: 'Action',
      price: '$59.99',
      tag: 'Best seller',
      imageUrl: '/images/games/ghost-of-yotei.png',
    },
    {
      name: 'Assassins Creed Shadows',
      category: 'RPG',
      price: '$49.99',
      tag: 'New',
      imageUrl: '/images/games/ac-shadows.png',
    },
    {
      name: 'War Thunder',
      category: 'Strategy',
      price: '$39.99',
      tag: 'Deal',
      imageUrl: '/images/games/war_thunder.png',
    },
  ];

  benefits = ['Curated titles', 'Digital-ready checkout', 'Fresh releases'];
}
