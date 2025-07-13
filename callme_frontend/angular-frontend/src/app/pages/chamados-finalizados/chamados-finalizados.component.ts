// Em chamados-finalizados.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { ChamadosItem  } from '../../models/Chamados.models';

@Component({
  selector: 'app-chamados-finalizados',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './chamados-finalizados.component.html',
  styleUrls: ['./chamados-finalizados.component.scss']
})
export class ChamadosFinalizadosComponent implements OnInit {

  chamadosFinalizados: ChamadosItem[] = [];
  isLoading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<ChamadosItem[]>('http://localhost:8080/chamados/finalizados').subscribe({
      next: (data) => {
        this.chamadosFinalizados = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar chamados finalizados:', err);
        this.isLoading = false;
      }
    });
  }
}
