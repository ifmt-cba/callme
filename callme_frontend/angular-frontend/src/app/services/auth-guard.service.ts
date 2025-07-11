// callme_frontend/angular-frontend/src/app/services/auth-guard.service.ts
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {

    // 1. Usa o AuthService para verificar se está autenticado (procurando pela chave 'accessToken')
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/principal']);
      return false;
    }

    // 2. Verifica se a rota exige roles específicas
    const requiredRoles = route.data['roles'] as string[];
    if (requiredRoles && requiredRoles.length > 0) {
      if (requiredRoles.some(role => this.authService.hasRole(role))) {
        return true; // Tem a role necessária
      } else {
        // Se não tem a role, nega o acesso e redireciona
        console.error('Acesso negado - Role não autorizada');
        this.router.navigate(['/home']); // Vai para home, mas sem acesso ao menu restrito
        return false;
      }
    }

    // Se a rota não exige roles, só precisa estar logado
    return true;
  }
}
