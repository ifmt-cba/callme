import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { LoginResponse } from "../types/login-response.type";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private authApiUrl = "http://localhost:8080";
  private passwordApiUrl = "http://localhost:8080/api/password";

  constructor(private httpClient: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.authApiUrl}/login`, { username, password });
  }

  signup(username: string, password: string, email: string): Observable<any> { // Mudado para Observable<any> para flexibilidade
    return this.httpClient.post<any>(`${this.authApiUrl}/users`, { username, password, email });
  }

  resetsenha(email: string): Observable<{ message: string }> {
    const body = { email: email };
    return this.httpClient.post<{ message: string }>(`${this.passwordApiUrl}/forgot`, body);
  }
}
