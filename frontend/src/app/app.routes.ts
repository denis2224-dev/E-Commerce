import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';
import { Products } from './pages/products/products';
import { CreateAccount } from './pages/create-account/create-account';
import { Account } from './pages/account/account';
import { Wishlist } from './pages/wishlist/wishlist';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'sign-in', component: SignIn },
  { path: 'create-account', component: CreateAccount },
  { path: 'account', component: Account },
  { path: 'games', component: Products },
  { path: 'products', redirectTo: 'games', pathMatch: 'full' },
  { path: 'wishlist', component: Wishlist },
  { path: '**', redirectTo: '' },
];
