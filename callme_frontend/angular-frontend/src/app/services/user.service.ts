import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/usuarios.models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('acessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl, {
      headers: this.getAuthHeaders()
    });
  }

  createUser(user: any, role: string): Observable<User> {
    const endpoint = role === 'ADMIN'
      ? `${this.baseUrl}/admin`
      : `${this.baseUrl}`;

    return this.http.post<User>(endpoint, user, {
      headers: this.getAuthHeaders()
    });
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  updateUser(user: User): Observable<User> {
    const url = `${this.baseUrl}/update/${user.id}`;
    const body = {
      username: user.username,
      email: user.email,
      password: user.password || null
    };

    console.log('PUT Request:', {
      url,
      body,
      headers: this.getAuthHeaders().keys()
    });

    return this.http.put<User>(
      url,
      body,
      {
        headers: this.getAuthHeaders(),
        observe: 'body',
        responseType: 'json'
      }
    );
  }
}
