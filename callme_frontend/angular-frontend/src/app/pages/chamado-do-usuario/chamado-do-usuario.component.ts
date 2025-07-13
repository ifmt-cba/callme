import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { ToastrService } from 'ngx-toastr'; // Importe o ToastrService

// As interfaces podem ser movidas para um arquivo models.ts
export interface Tecnico {
  userid: string;
  username: string;
}
export interface Comentario {
  id: number;
  texto: string;
  dataCriacao: string;
  autor: {
    username: string;
  };
}
export interface Chamado {
  id: number;
  remetente: string;
  assunto: string;
  descricao: string;
  dataHora: string;
  status: 'ABERTO' | 'EM_ANDAMENTO' | 'FECHADO' | 'CANCELADO';
  tecnico?: Tecnico | null;
  comentarios?: Comentario[]; }

@Component({
  selector: 'app-meus-chamados',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './chamado-do-usuario.component.html', // Corrija se o nome do seu arquivo for diferente
  styleUrls: ['./chamado-do-usuario.component.scss']
})
export class ChamadoDoUsuarioComponent implements OnInit {

  meusChamados: Chamado[] = [];
  isLoading = true;
  chamadoExpandidoId: number | null = null;

  // Guarda o texto do novo comentário PARA CADA chamado
  comentarios: { [chamadoId: number]: string } = {};

  private apiUrl = 'http://localhost:8080/chamados';

  constructor(
    private http: HttpClient,
    private toastService: ToastrService // Injete o ToastrService
  ) {}

  ngOnInit(): void {
    this.carregarMeusChamados();
  }

  carregarMeusChamados(): void {
    this.isLoading = true;
    this.http.get<Chamado[]>(`${this.apiUrl}/meus-chamados`).subscribe({
      next: (data) => {
        this.meusChamados = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar meus chamados:', err);
        this.toastService.error('Falha ao carregar seus chamados.');
        this.isLoading = false;
      }
    });
  }

  toggleDetalhes(id: number): void {
    this.chamadoExpandidoId = (this.chamadoExpandidoId === id) ? null : id;
  }

  /**
   * Salva as alterações de status e o novo comentário para um chamado.
   * @param chamado O objeto do chamado que foi modificado na tela.
   */
  salvarAlteracoes(chamado: Chamado): void {
    const comentario = this.comentarios[chamado.id] || '';

    // Prepara os dados para enviar ao backend
    const payload = {
      status: chamado.status,
      comentario: comentario.trim()
    };

    console.log(`Salvando alterações para o chamado ID ${chamado.id}`, payload);

    // --- LÓGICA DA API (será criada no backend) ---
    // O endpoint ideal seria específico para esta ação
    this.http.put(`${this.apiUrl}/${chamado.id}/tecnico-update`, payload).subscribe({
      next: () => {
        this.toastService.success('Chamado atualizado com sucesso!');
        this.comentarios[chamado.id] = ''; // Limpa o campo de comentário após salvar
        // Opcional: recolher o card após salvar
        // this.chamadoExpandidoId = null;
      },
      error: (err) => {
        console.error('Erro ao salvar alterações:', err);
        this.toastService.error('Não foi possível salvar as alterações.');
      }
    });
  }
}
