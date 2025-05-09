import { Routes } from '@angular/router';
import {DefaultLoginLayoutComponent} from "./components/default-login-layout/default-login-layout.component";
import {LoginComponent} from "./pages/login/login.component";
import {SignupComponent} from "./pages/signup/signup.component";
import {HomeComponent} from "./pages/home/home.component";
import {AuthGuard} from "./services/auth-guard.service";
import {ResetSenhaComponent} from "./pages/resetsenha/reset-senha.component";
import {ChamadosInternosComponent} from "./pages/chamados-internos/chamados-internos.component";

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },

  {
    path: 'signup',
    component: SignupComponent,
  },

  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard],
  },

  {
    path: 'resetsenha',
    component: ResetSenhaComponent,
  },

  {
    path: 'ChamadosInternos',
    component: ChamadosInternosComponent,
  },
];
