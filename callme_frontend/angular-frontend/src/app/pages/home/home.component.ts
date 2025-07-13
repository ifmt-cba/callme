import { Component, OnInit } from '@angular/core';
import { FeedService } from "../../services/feed.service";
import { Router } from "@angular/router";
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { SlicePipe, NgForOf, NgIf, NgClass, DatePipe } from "@angular/common";
import { ChamadoUnificado } from '../../models/chamado-unificado.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    SlicePipe,
    NgForOf,
    NgIf,
    NgClass,
    DatePipe,
    FormsModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  chamadosUnificados: ChamadoUnificado[] = [];
  isSideNavCollapsed = false;
  screenWidth = 0;

  // Propriedades para os filtros
  searchTerm: string = '';
  selectedStatus: string = '';
  selectedType: string = '';

  constructor(private feedService: FeedService, private router: Router) {}

  ngOnInit(): void {
    this.feedService.getChamadosUnificados().subscribe({
      next: (res) => { this.chamadosUnificados = res; },
      error: (err) => { console.error('Erro ao carregar chamados:', err); }
    });
  }

  // ✅ LÓGICA DE FILTRO CORRIGIDA E MELHORADA
  get filteredChamados(): ChamadoUnificado[] {
    // Usamos .filter() uma única vez para combinar todas as condições
    return this.chamadosUnificados.filter(chamado => {

      // Condição 1: Filtro por Status
      const statusMatch = !this.selectedStatus || chamado.status === this.selectedStatus;

      // Condição 2: Filtro por Tipo
      const typeMatch = !this.selectedType || chamado.tipo === this.selectedType;

      // Condição 3: Filtro por Termo de Busca (pesquisa em todos os campos)
      const searchTermMatch = !this.searchTerm || this.checkSearchTerm(chamado, this.searchTerm);

      // O chamado só aparece se passar em TODAS as condições
      return statusMatch && typeMatch && searchTermMatch;
    });
  }

  // ✅ NOVA FUNÇÃO AUXILIAR PARA PESQUISA UNIVERSAL E SEGURA
  private checkSearchTerm(chamado: ChamadoUnificado, term: string): boolean {
    const lowerCaseTerm = term.toLowerCase();

    // Concatena todos os campos relevantes em uma única string e verifica se o termo está nela.
    // O `?.` (optional chaining) garante que o código não quebre se um campo for nulo.
    const allChamadoData = [
      chamado.id,
      chamado.assunto,
      chamado.descricao,
      chamado.solicitante,
      chamado.status,
      chamado.tipo
    ].join(' ').toLowerCase(); // Junta tudo numa "frase" em minúsculas

    return allChamadoData.includes(lowerCaseTerm);
  }

  onToggleSideNav(data: any): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  navigateToExternalDetails(id: string): void {
    this.router.navigate(['/buscar', id]);
  }
}
