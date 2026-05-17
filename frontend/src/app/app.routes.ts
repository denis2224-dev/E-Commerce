import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { SignIn } from './pages/sign-in/sign-in';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'sign-in', component: SignIn },
  { path: '**', redirectTo: '' },
];
