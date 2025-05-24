// src/app/services/user.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/users'; // ajuste para sua API

  constructor(private http: HttpClient) {}

  getUserProfile(): Observable<any> {
    return this.http.get(`${this.API_URL}/me`);
  }

  updateUserProfile(data: any): Observable<any> {
    return this.http.put(`${this.API_URL}/me`, data);
  }

  listUsers(): Observable<any[]> {
    return this.http.get<any[]>(this.API_URL);
  }

  createUser(user: any): Observable<any> {
    return this.http.post(this.API_URL, user);
  }
}
