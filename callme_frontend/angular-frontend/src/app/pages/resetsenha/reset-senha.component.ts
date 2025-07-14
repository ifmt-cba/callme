import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from '../../services/login.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-resetsenha',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reset-senha.html',
  styleUrls: ['./reset-senha.scss']
})
export class ResetsenhaComponent {

  emailForm: FormGroup;
  isLoading = false;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastr: ToastrService
  ) {
    this.emailForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    });
  }

  submitEmail(): void {
    if (this.emailForm.invalid) {
      this.toastr.warning('Por favor, insira um e-mail válido.');
      return;
    }

    this.isLoading = true;
    const email = this.emailForm.get('email')?.value;

    this.loginService.resetsenha(email).subscribe({
      // ✅ CORREÇÃO: Agora o tipo 'response' bate com o retorno do serviço
      next: (response: { message: string }) => {
        this.toastr.success(response.message);
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        const errorMessage = err.error?.message || 'Ocorreu um erro desconhecido.';
        this.toastr.error(errorMessage);
        this.isLoading = false;
      }
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['/principal']);
  }
}
