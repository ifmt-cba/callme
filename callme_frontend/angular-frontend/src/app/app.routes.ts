import { Routes } from '@angular/router';
import {DefaultLoginLayoutComponent} from "./components/default-login-layout/default-login-layout.component";
import {LoginComponent} from "./pages/login/login.component";


export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  }
];
