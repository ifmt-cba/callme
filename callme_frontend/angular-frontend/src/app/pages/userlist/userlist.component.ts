import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';  // importe HttpHeaders
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Pipe({
  name: 'filtroUsuarios',
  standalone: true
})
export class FiltroUsuariosPipe implements PipeTransform {
  transform(usuarios: any[], filtro: string): any[] {
    if (!filtro) return usuarios;
    return usuarios.filter(user =>
      user.username.toLowerCase().includes(filtro.toLowerCase())
    );
  }
}

@Component({
  selector: 'app-userlist',
  standalone: true,
  imports: [CommonModule, FormsModule, FiltroUsuariosPipe],
  templateUrl: './userlist.component.html',
  styleUrls: ['./userlist.component.scss']
})
export class UserListComponent implements OnInit {
  usuarios: any[] = [];
  filtro = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // pega o token do localStorage
    const token = localStorage.getItem('authToken');

    // cria headers com Authorization Bearer token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    // passa os headers na requisição GET
    this.http.get<any[]>('http://localhost:8080/users', { headers }).subscribe({
      next: (res) => (this.usuarios = res),
      error: (err) => console.error('Erro ao carregar usuários:', err)
    });
  }
}
