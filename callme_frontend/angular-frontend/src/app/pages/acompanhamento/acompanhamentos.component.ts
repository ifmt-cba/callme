import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { FeedService } from '../../services/feed.service';
import { Acompanhamento } from '../../models/acompanhamento.model';
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-acompanhamentos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  // ✅ CORREÇÃO CRÍTICA: O templateUrl deve apontar para o seu próprio HTML.
  templateUrl: './acompanhamentos.component.html',
  styleUrls: ['./acompanhamentos.component.scss']
})
export class AcompanhamentosComponent {
  token: string = '';
  chamado: Acompanhamento | null = null;
  isLoading: boolean = false;
  erro: string | null = null;

  constructor(
    private feedService: FeedService,
    private toastr: ToastrService
  ) {}

  buscarChamado(): void {
    const tokenLimpo = this.token.trim().replace(/"/g, '');

    if (!tokenLimpo) {
      this.erro = 'Por favor, insira um token válido.';
      return;
    }

    this.isLoading = true;
    this.erro = null;
    this.chamado = null;

    this.feedService.getAcompanhamentoPorToken(tokenLimpo).subscribe({
      next: (resultado: Acompanhamento) => {
        this.chamado = resultado;
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 404) {
          this.erro = 'Chamado não encontrado. Verifique o token e tente novamente.';
        } else {
          this.erro = 'Ocorreu um erro ao buscar seu chamado. Tente mais tarde.';
        }
        this.isLoading = false;
      }
    });
  }
}
