
import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { LoginResponse } from "../types/login-response.type";
import { Observable } from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  apiUrl: string = "http://localhost:8080";

  constructor(private httpClient: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(this.apiUrl + "/login", { username, password });
  }

  signup(username: string, password: string, email: string){
    return this.httpClient.post<LoginResponse>(this.apiUrl + "/users", { username, password, email })
  }

  resetsenha(email: string){
    return this.httpClient.post<LoginResponse>(this.apiUrl + "/api/password/forgot", { email })
  }
}
