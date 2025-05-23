import { Routes } from '@angular/router';
import {DefaultLoginLayoutComponent} from "./components/default-login-layout/default-login-layout.component";
import {LoginComponent} from "./pages/login/login.component";
import {SignupComponent} from "./pages/signup/signup.component";
import {HomeComponent} from "./pages/home/home.component";
import {AuthGuard} from "./services/auth-guard.service";
import {ResetSenhaComponent} from "./pages/resetsenha/reset-senha.component";
import {ChamadosInternosComponent} from "./pages/chamados-internos/chamados-internos.component";
import {ChamadosExternosComponent} from "./pages/chamados-externos/chamados-externos.component";
import {CriarChamadosComponent} from "./pages/criar-chamados/criar-chamados.component";
import {UsuariosComponent} from "./pages/usuarios/usuarios.component";
import {AcompanhamentoComponent} from "./pages/acompanhamento/acompanhamento.component";
import { EditarChamadoComponent } from './pages/editar-chamado/editar-chamado.component';
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
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

  {
    path: 'chamadosExternos',
    component: ChamadosExternosComponent,

  },

  {
    path: 'CriarChamados',
    component: CriarChamadosComponent,
  },

  {
    path: 'principal',
    component: UsuariosComponent,
  },
  {
    path:'acompanhamentos',
    component: AcompanhamentoComponent,
  },

  {
    path: 'buscar/:tokenEmail',
    component: EditarChamadoComponent
  },

  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },

];
