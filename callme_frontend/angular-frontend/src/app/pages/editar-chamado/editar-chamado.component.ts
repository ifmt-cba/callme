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

// Interface para o Chamado, incluindo o técnico opcional
interface Chamado {
  id: number;
  remetente: string;
  assunto: string;
  descricao: string;
  status: 'ABERTO' | 'EM_ANDAMENTO' | 'FECHADO';
  tokenEmail: string;
  tecnico?: User | null; // O técnico pode não estar definido
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
  salvar(): void {
    console.log("%cVerificando localStorage ANTES de salvar:", "color: blue; font-weight: bold;");
    console.log("Valor de 'acessToken' é:", localStorage.getItem('acessToken'));

    if (!this.chamado) {
      alert('Dados do chamado não carregados.');
      return;
    }

    // Monta o objeto de dados PARA ENVIAR (payload)
    const dadosParaSalvar = {

      status: this.chamado.status,
      // Verifica se um técnico foi selecionado. Se sim, cria o objeto { id: ... }. Se não, envia null.
      tecnico: this.tecnicoIdSelecionado ? { id: this.tecnicoIdSelecionado } : null
    };

    console.log('Enviando para o backend:', dadosParaSalvar); // Ótimo para debugar!

    // Envia o novo objeto 'dadosParaSalvar' em vez de 'this.chamado'
    this.http.put(`${this.apiUrl}/editar/token/${this.tokenEmail}`, dadosParaSalvar).subscribe({
      next: () => {
        alert('Chamado atualizado com sucesso!');
        this.router.navigate(['/chamados']); // Redireciona para a lista de chamados
      },
      error: (error) => {
        console.error('Erro ao salvar chamado:', error);
        alert('Erro ao salvar chamado.');
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
