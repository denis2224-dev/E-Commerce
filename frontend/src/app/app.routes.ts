import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';
import { Products } from './pages/products/products';
import { CreateAccount } from './pages/create-account/create-account';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'sign-in', component: SignIn },
  { path: 'create-account', component: CreateAccount },
  { path: 'games', component: Products },
  { path: 'products', redirectTo: 'games', pathMatch: 'full' },
  { path: '**', redirectTo: '' },
];
