import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';

// Interfaces (estão corretas)
export interface Tecnico {
  userid: string;
  username: string;
}

export interface Chamado {
  id: number;
  remetente: string;
  assunto: string;
  descricao: string;
  dataHora: string;
  status: 'ABERTO' | 'EM_ANDAMENTO' | 'FECHADO' | 'CANCELADO';
  tecnico?: Tecnico | null;
}

@Component({
  selector: 'app-meus-chamados', // O seletor correto para este componente
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './chamado-do-usuario.component.html',
  styleUrls: ['./chamado-do-usuario.component.scss']
})
export class ChamadoDoUsuarioComponent implements OnInit {

  meusChamados: Chamado[] = [];
  isLoading = true;

  chamadoExpandidoId: number | null = null;
  comentarioAbertoId: number | null = null;
  comentarioAtual: string = '';

  private apiUrl = 'http://localhost:8080/chamados/meus-chamados';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    console.log('[MeusChamados] Componente iniciado. Buscando dados da API...');
    this.http.get<Chamado[]>(this.apiUrl).subscribe({
      next: (data) => {
        console.log('%c[MeusChamados] Dados recebidos com sucesso!', 'color: green', data);
        this.meusChamados = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('%c[MeusChamados] Erro ao carregar chamados:', 'color: red', err);
        this.isLoading = false;
      }
    });
  }

  toggleDetalhes(id: number): void {
    const estavaAberto = this.chamadoExpandidoId === id;
    this.chamadoExpandidoId = estavaAberto ? null : id;
    console.log(`[MeusChamados] Toggle Detalhes: Chamado ID ${id} foi ${estavaAberto ? 'recolhido' : 'expandido'}.`);

    if (this.chamadoExpandidoId !== id) {
      this.comentarioAbertoId = null;
    }
  }

  toggleComentario(id: number): void {
    const estavaAberto = this.comentarioAbertoId === id;
    this.comentarioAbertoId = estavaAberto ? null : id;
    this.comentarioAtual = '';
    console.log(`[MeusChamados] Toggle Comentário: Formulário para o chamado ID ${id} foi ${estavaAberto ? 'fechado' : 'aberto'}.`);
  }

  enviarComentario(chamadoId: number): void {
    if (!this.comentarioAtual.trim()) {
      alert('O comentário não pode estar vazio.');
      return;
    }
    console.log(`%c[MeusChamados] SIMULANDO ENVIO: Comentário para o chamado ${chamadoId}: "${this.comentarioAtual}"`, 'color: orange');
    alert('Comentário enviado (simulação)');
    this.comentarioAbertoId = null;
    this.comentarioAtual = '';
  }

  fecharChamado(chamadoId: number): void {
    if (confirm('Tem certeza que deseja fechar este chamado?')) {
      console.log(`%c[MeusChamados] SIMULANDO AÇÃO: Fechando o chamado ${chamadoId}`, 'color: red');
      alert('Chamado fechado (simulação)');
    }
  }
}
