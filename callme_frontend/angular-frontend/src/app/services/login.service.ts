import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoginResponse} from "../types/login-response.type";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
apiUrl: string = "http://localhost:8080/login";
  constructor(private httpClient: HttpClient) {}

  login(username: string, password: string){
    return this.httpClient.post<LoginResponse>(this.apiUrl, { username, password }).pipe(
      tap((value) => {
        sessionStorage.setItem("auth-token", value.token)
        sessionStorage.setItem("username", value.username)
      })
    )
  }
}
