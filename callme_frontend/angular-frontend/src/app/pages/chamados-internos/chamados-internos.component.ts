import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms'; // 1. Importe o FormsModule

// As interfaces FeedItem e FeedResponse permanecem as mesmas
export interface FeedItem {
  chamadoId: number;
  content: string;
  descricao: string | null;
  authorName: string;
}

export interface FeedResponse {
  feedItens: FeedItem[];
  page: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

@Component({
  selector: 'app-chamados-internos',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    DatePipe,
    FormsModule // 2. Adicione FormsModule aos imports
  ],
  templateUrl: './chamados-internos.component.html',
  styleUrls: ['./chamados-internos.component.scss']
})
export class ChamadosInternosComponent implements OnInit {

  private apiUrl = 'http://localhost:8080';
  private allChamados: FeedItem[] = [];
  isLoading = true;

  // 3. Propriedades para o filtro (igual à home)
  searchTerm: string = '';

  // 4. Propriedades para controlar a navbar (igual à home)
  isSideNavCollapsed = false;
  screenWidth = 0;

  constructor(private http: HttpClient, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.http.get<FeedResponse>(`${this.apiUrl}/feed`).subscribe({
      next: (data) => {
        this.allChamados = data.feedItens;
        this.isLoading = false;
      },
      error: (err) => {
        this.toastr.error('Falha ao carregar os chamados do feed.');
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  // 5. Getter para filtrar os chamados (igual à home)
  get filteredChamados(): FeedItem[] {
    if (!this.searchTerm) {
      return this.allChamados;
    }
    return this.allChamados.filter(chamado => this.checkSearchTerm(chamado, this.searchTerm));
  }

  // 6. Função auxiliar de busca (adaptada para os campos do FeedItem)
  private checkSearchTerm(chamado: FeedItem, term: string): boolean {
    const lowerCaseTerm = term.toLowerCase().trim();

    const idToSearch = lowerCaseTerm.startsWith('#') ? lowerCaseTerm.substring(1) : lowerCaseTerm;
    if (!isNaN(Number(idToSearch))) {
      return chamado.chamadoId.toString() === idToSearch;
    }

    const allChamadoData = [
      chamado.content,
      chamado.descricao,
      chamado.authorName
    ].join(' ').toLowerCase();

    return allChamadoData.includes(lowerCaseTerm);
  }

  // 7. Função que recebe o evento da navbar (igual à home)
  onToggleSideNav(data: { collapsed: boolean, screenWidth: number }): void {
    this.isSideNavCollapsed = data.collapsed;
    this.screenWidth = data.screenWidth;
  }
}
