// callme_frontend/angular-frontend/src/app/services/auth.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // Esta é a chave PADRÃO que vamos usar em todo o sistema.
  private readonly TOKEN_KEY = 'accessToken';

  constructor() {}

  setToken(token: string): void {
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  removeToken(): void {
    sessionStorage.removeItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // Adicione estes métodos para gerenciar as roles
  getUserRoles(): string[] {
    const token = this.getToken();
    if (!token) {
      return [];
    }
    try {
      const payloadBase64 = token.split('.')[1];
      const decodedPayload = atob(payloadBase64);
      const payloadJson = JSON.parse(decodedPayload);
      const roles = payloadJson.scope ? payloadJson.scope.split(' ') : [];
      return roles;
    } catch (error) {
      console.error('Erro ao decodificar o token:', error);
      return [];
    }
  }

  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  logout(): void {
    this.removeToken();
  }
}
