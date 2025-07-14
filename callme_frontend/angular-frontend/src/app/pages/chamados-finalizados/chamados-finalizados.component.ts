  // Em chamados-finalizados.component.ts
  import { Component, OnInit } from '@angular/core';
  import {CommonModule, DatePipe} from '@angular/common';
  import { HttpClient } from '@angular/common/http';
  import { NavbarComponent } from '../../components/navbar/navbar.component';
  import { ChamadosItem  } from '../../models/Chamados.models';

  @Component({
    selector: 'app-chamados-finalizados',
    standalone: true,
    imports: [CommonModule, NavbarComponent, DatePipe], // Adicione DatePipe aqui
    templateUrl: './chamados-finalizados.component.html',
    styleUrls: ['./chamados-finalizados.component.scss']
  })
  export class ChamadosFinalizadosComponent implements OnInit {

    chamadosFinalizados: ChamadosItem[] = [];
    isLoading = true;

    constructor(private http: HttpClient) {}

    ngOnInit(): void {
      console.log('[Finalizados] Componente iniciado, buscando chamados finalizados...');
      this.http.get<ChamadosItem[]>('http://localhost:8080/chamados/finalizados').subscribe({
        next: (data) => {
          console.log('%c[Finalizados] Dados recebidos com sucesso!', 'color: green', data);
          this.chamadosFinalizados = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('%c[Finalizados] Erro ao carregar chamados finalizados:', 'color: red', err);
          this.isLoading = false;
        }
      });
    }
  }
