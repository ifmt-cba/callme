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
import {AcompanhamentosComponent} from "./pages/acompanhamento/acompanhamentos.component";
import { EditarChamadoComponent } from './pages/editar-chamado/editar-chamado.component';
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
import {UserlistersComponent} from "./pages/userlisters/userlisters.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {ChamadoDoUsuarioComponent} from "./pages/chamado-do-usuario/chamado-do-usuario.component";
import {ChamadosFinalizadosComponent} from "./pages/chamados-finalizados/chamados-finalizados.component";

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/principal',
    pathMatch: 'full'
  },
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
    component: AcompanhamentosComponent,
  },

  {
    path: 'buscar/:tokenEmail',
    component: EditarChamadoComponent
  },

  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path: 'listar-usuarios',
    component :UserlistersComponent,
    canActivate : [AuthGuard],
    data :{
      roles : ['ADMIN']
    }
  },
  {
    path: 'meus-chamados',
    component: ChamadoDoUsuarioComponent,
    canActivate: [AuthGuard]
  },

  {
    path : 'Dashboard',
    component :DashboardComponent,
  },
  {
    path: 'chamados-finalizados',
    component: ChamadosFinalizadosComponent,
    canActivate: [AuthGuard]
  },

];
