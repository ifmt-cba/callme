// callme_frontend/angular-frontend/src/app/security/token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  // Pega o token do sessionStorage usando a chave CORRETA
  const token = sessionStorage.getItem('accessToken'); // <== CORREÇÃO AQUI

  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }

  // Este aviso não é um erro, apenas informa que a requisição está indo sem token
  console.warn('[Interceptor] Nenhum token encontrado no sessionStorage.');
  return next(req);
};
