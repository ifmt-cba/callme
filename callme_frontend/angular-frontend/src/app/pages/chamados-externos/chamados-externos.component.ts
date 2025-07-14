import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

// ✅ CORREÇÃO: Caminhos e nomes de arquivos ajustados para o padrão do projeto.
import { ChamadoExterno } from '../../models/chamado-externo.model';
import { User } from '../../models/usuarios.models';
import {ChamadoExternoService} from "../../services/chamados-externos.service";
import { NavbarComponent } from '../../components/navbar/navbar.component';
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent, RouterModule], // ✅ Adicione RouterModule aqui
  templateUrl: './chamados-externos.component.html',
  styleUrls: ['./chamados-externos.component.scss']
})
export class ChamadosExternosComponent implements OnInit {
  chamados: ChamadoExterno[] = [];
  tecnicos: User[] = [];
  isLoading = true;

  // ✅ PROPRIEDADES PARA OS FILTROS ADICIONADAS
  termoPesquisa: string = '';
  statusFiltro: string = '';

  constructor(
    private chamadoExternoService: ChamadoExternoService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.carregarChamados();
  }

  // ✅ GETTER PARA A LISTA FILTRADA QUE O HTML VAI USAR
  get chamadosFiltrados(): ChamadoExterno[] {
    return this.chamados.filter((chamado: ChamadoExterno) => {
      const correspondeTermo = !this.termoPesquisa ||
        chamado.assunto?.toLowerCase().includes(this.termoPesquisa.toLowerCase()) ||
        chamado.remetente?.toLowerCase().includes(this.termoPesquisa.toLowerCase());

      const correspondeStatus = !this.statusFiltro || chamado.status === this.statusFiltro;

      return correspondeTermo && correspondeStatus;
    });
  }

  carregarChamados(): void {
    this.isLoading = true;
    this.chamadoExternoService.listarChamados().subscribe({
      next: (data: ChamadoExterno[]) => {
        this.chamados = data;
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error('Falha ao carregar os chamados externos.', 'Erro');
        console.error('Erro ao carregar chamados:', err);
        this.isLoading = false;
      }
    });
  }

  atribuirTecnico(chamadoId: number, tecnicoId: string): void {
    if (!tecnicoId) {
      this.toastr.warning('Por favor, selecione um técnico.');
      return;
    }
    console.log(`Atribuir técnico ${tecnicoId} ao chamado ${chamadoId}`);
  }
}
