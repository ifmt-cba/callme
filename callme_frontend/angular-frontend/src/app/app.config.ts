import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {provideToastr} from "ngx-toastr";
import {provideAnimations} from "@angular/platform-browser/animations";
import {tokenInterceptor} from "./security/token.interceptor"

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withFetch()),
    provideAnimations(),
    provideToastr(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([tokenInterceptor]))
  ]

};

