import { Component } from '@angular/core';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms"; // Importe os módulos de formulário
import { CommonModule } from "@angular/common";
import { ChamadoInternoService } from "../../services/chamado-interno.service"; // Importe o novo serviço
import { ToastrService } from "ngx-toastr";
import { Router } from "@angular/router";

@Component({
  selector: 'app-criar-chamados',
  standalone: true,
  imports: [
    NavbarComponent,
    ReactiveFormsModule, // Adicione o ReactiveFormsModule
    CommonModule
  ],
  templateUrl: './criar-chamados.component.html',
  styleUrls: ['./criar-chamados.component.scss']
})
export class CriarChamadosComponent {
  chamadoForm: FormGroup;
  mensagemSucesso = false;

  constructor(
    private fb: FormBuilder,
    private chamadoService: ChamadoInternoService,
    private toastr: ToastrService,
    private router: Router
  ) {
    // Cria o formulário com os dois campos
    this.chamadoForm = this.fb.group({
      content: ['', Validators.required],
      descricao: ['', Validators.required]
    });
  }

  // Método para enviar os dados
  onSubmit(): void {
    if (this.chamadoForm.invalid) {
      this.toastr.warning('Por favor, preencha todos os campos.');
      return;
    }

    this.chamadoService.criarChamado(this.chamadoForm.value).subscribe({
      next: () => {
        this.toastr.success('Chamado interno criado com sucesso!');
        this.mensagemSucesso = true;
        this.chamadoForm.reset();
        // Redireciona para a home após 2 segundos
        setTimeout(() => this.router.navigate(['/home']), 2000);
      },
      error: (err) => {
        this.toastr.error('Erro ao criar chamado. Tente novamente.');
        console.error(err);
      }
    });
  }
}
