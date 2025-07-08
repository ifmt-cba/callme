import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  // Pega o token do localStorage
  const token = sessionStorage.getItem('acessToken');

  // Se o token existir, clona a requisição e adiciona o cabeçalho
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }
  console.warn('[Interceptor] Nenhum token encontrado no localStorage.');
  // Se não, passa a requisição original
  return next(req);
};
