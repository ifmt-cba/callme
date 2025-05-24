import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reset-senha',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
  ],
  templateUrl: './reset-senha.html',
  styleUrl: './reset-senha.scss'
})
export class ResetSenhaComponent {
  resetForm!: FormGroup;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastService: ToastrService
  ) {
    this.resetForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
    });
  }

  submit() {
    this.loginService.resetsenha(this.resetForm.value.email).subscribe({
      next: () => {
        this.toastService.success('Solicitação enviada com sucesso!');
        setTimeout(() => this.router.navigate(['/principal']), 1200);
      },
      error: () => this.toastService.error('Erro ao enviar solicitação. Tente novamente mais tarde.'),
    });
  }
}
