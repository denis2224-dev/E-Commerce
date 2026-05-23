import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
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
  { path: 'games', component: Products },
  { path: 'products', redirectTo: 'games', pathMatch: 'full' },
  { path: 'account', component: Account, canActivate: [authGuard] },
  { path: 'wishlist', component: Wishlist, canActivate: [authGuard] },
  // { path: 'checkout', component: Checkout, canActivate: [authGuard] },
  // { path: 'orders', component: Orders, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
