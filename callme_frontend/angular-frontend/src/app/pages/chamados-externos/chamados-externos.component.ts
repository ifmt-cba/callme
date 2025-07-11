import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChamadosExternosService } from '../../services/chamados-externos.service';
import { ChamadosItem, ChamadoResponse } from '../../models/Chamados.models';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';
import {RouterLink} from "@angular/router";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule, NavbarComponent, HttpClientModule, RouterLink, FormsModule],
  templateUrl: './chamados-externos.component.html',
  styleUrls: ['./chamados-externos.component.scss']
})
export class ChamadosExternosComponent implements OnInit {
  chamado: ChamadosItem[] = [];

  termoPesquisa: string = '';
  statusFiltro: string = '';

  constructor(private chamadosExternosService: ChamadosExternosService) {}

  ngOnInit(): void {
    this.chamadosExternosService.getFeed().subscribe({
      next: (res: ChamadosItem[]) => {
        this.chamado = res;
      },
      error: (err) => {
        console.error('Erro ao carregar chamados:', err);
        this.chamado = [];
      }
    });
  }

  get chamadosFiltrados(): ChamadosItem[] {
    // Começa com a lista completa
    let items = this.chamado;

    // 1. Aplica o filtro de STATUS
    if (this.statusFiltro) {
      items = items.filter(item => item.status === this.statusFiltro);
    }

    // 2. Aplica a PESQUISA de texto no resultado do filtro anterior
    if (this.termoPesquisa) {
      const termo = this.termoPesquisa.toLowerCase();
      items = items.filter(item =>
        item.assunto.toLowerCase().includes(termo) ||
        item.remetente.toLowerCase().includes(termo) ||
        (item.tecnico && item.tecnico.username.toLowerCase().includes(termo))
      );
    }

    return items;
  }
}
