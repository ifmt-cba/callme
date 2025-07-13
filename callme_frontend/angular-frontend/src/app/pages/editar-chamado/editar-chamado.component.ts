import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from "@angular/forms";
import { NavbarComponent } from "../../components/navbar/navbar.component";

// --- INTERFACES PARA TIPAGEM (Boa prática) ---
// Adicionei a interface do Usuário/Técnico baseada no que discutimos
interface User {
  id: string;
  username: string;
  // outros campos que sua API retornar...
}

export interface Comentario {
  id: number;
  texto: string;
  dataCriacao: string;
  autor: {
    username: string;
  };
}

// Interface para o Chamado, incluindo o técnico opcional
interface Chamado {
  id: number;
  remetente: string;
  assunto: string;
  descricao: string;
  status: 'ABERTO' | 'EM_ANDAMENTO' | 'FECHADO';
  tokenEmail: string;
  tecnico?: User | null; // O técnico pode não estar definido
  comentarios?: Comentario[];
}


@Component({
  selector: 'app-editar-chamado',
  templateUrl: './editar-chamado.component.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarComponent
  ],
  styleUrls: ['./editar-chamado.component.scss']
})
export class EditarChamadoComponent implements OnInit {
  // --- PROPRIEDADES DO COMPONENTE ---
  chamado: Chamado | null = null;
  tokenEmail!: string;
  tecnicos: User[] = []; // Array para armazenar a lista de técnicos vinda da API
  tecnicoIdSelecionado: string | null = null; // Armazena APENAS o ID do técnico selecionado no dropdown

  private apiUrl = 'http://localhost:8080/chamados'; // Centralizando a URL da API

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Pega o token da URL assim que o componente inicia
    const token = this.route.snapshot.paramMap.get('tokenEmail');
    if (!token) {
      alert('Token do chamado não encontrado na URL.');
      this.router.navigate(['/']); // Redireciona se não houver token
      return;
    }
    this.tokenEmail = token;

    // Inicia o carregamento dos dados necessários
    this.carregarTecnicos();
    this.carregarChamado();
  }

  /**
   * Busca a lista de todos os usuários técnicos da API.
   */
  carregarTecnicos(): void {
    this.http.get<User[]>(`${this.apiUrl}/tecnicos`).subscribe({
      next: (data) => {
        this.tecnicos = data;
      },
      error: (err) => {
        console.error('Erro ao buscar a lista de técnicos:', err);
        alert('Não foi possível carregar a lista de técnicos. Verifique o console.');
      }
    });
  }

  /**
   * Busca os detalhes do chamado específico usando o token.
   */
  carregarChamado(): void {
    this.http.get<Chamado>(`${this.apiUrl}/buscar/${this.tokenEmail}`).subscribe({
      next: (data) => {
        this.chamado = data;
        // Se o chamado já tem um técnico, define o ID dele na nossa variável de controle
        // para que o dropdown já venha selecionado corretamente.
        if (this.chamado?.tecnico) {
          this.tecnicoIdSelecionado = this.chamado.tecnico.id;
        }
      },
      error: (err) => {
        console.error('Erro ao buscar chamado:', err);
        alert('Erro ao carregar os dados do chamado.');
      }
    });
  }

  /**
   * Prepara os dados e os envia para a API para salvar as alterações.
   */
  // Em editar-chamado.component.ts
  salvar(): void {
    if (!this.chamado) return;

    const dadosParaSalvar = {
      status: this.chamado.status,
      // Aqui está a mágica:
      // Pegamos o ID selecionado (que agora é this.tecnicoIdSelecionado)
      // E o enviamos na chave 'userid' que o backend espera.
      tecnico: this.tecnicoIdSelecionado
        ? { userid: this.tecnicoIdSelecionado }
        : null
    };

    console.log("ENVIANDO ESTE JSON PARA O BACKEND:", JSON.stringify(dadosParaSalvar, null, 2));

    this.http.put(`${this.apiUrl}/editar/token/${this.tokenEmail}`, dadosParaSalvar)
      .subscribe({
        next: (response) => {
          alert('Chamado atualizado com sucesso!');
          this.chamado = response as Chamado;
          if (this.chamado.tecnico) {
            // Se o backend retornar o técnico, atualize o dropdown.
            // Note que o backend retorna um objeto com 'userid', mas seu frontend agora usa 'id'.
            // Precisamos garantir que o objeto tecnico tenha a propriedade 'id'.
            // Uma melhor abordagem seria fazer o backend retornar o DTO TecnicoDTO que já tem 'id'.
            // Mas para consertar agora:
            this.tecnicoIdSelecionado = this.chamado.tecnico.id;
          } else {
            this.tecnicoIdSelecionado = null;
          }
        },
        error: (err) => {
          console.error('Erro ao salvar chamado:', err);
          alert('Falha ao salvar as alterações.');
        }
      });
  }
  /**
   * Exclui o chamado atual.
   */
  excluir(): void {
    if (this.chamado && confirm('Tem certeza que deseja excluir este chamado?')) {
      // Assumindo que a exclusão é por ID do chamado
      this.http.delete(`${this.apiUrl}/${this.chamado.id}`).subscribe({
        next: () => {
          alert('Chamado excluído com sucesso!');
          this.router.navigate(['/chamados']);
        },
        error: error => {
          console.error('Erro ao excluir chamado:', error);
          alert('Erro ao excluir.');
        }
      });
    }
  }
}
