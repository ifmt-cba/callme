import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  styleUrls: ['./reset-password.component.scss'],
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  token: string = '';
  message: string = '';
  error: string = '';

  requirements = {
    minLength: false,
    number: false,
    special: false,
    uppercase: false
  };

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient
  ) {
    this.form = this.fb.group({
      newPassword: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
    });
  }

  checkPassword(): void {
    const password = this.form.get('newPassword')?.value || '';

    this.requirements = {
      minLength: password.length >= 8,
      number: /[0-9]/.test(password),
      special: /[^A-Za-z0-9]/.test(password),
      uppercase: /[A-Z]/.test(password)
    };
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const newPassword = this.form.value.newPassword;

    const params = new HttpParams()
      .set('token', this.token)
      .set('newPassword', newPassword);

    this.http.post('http://localhost:8080/api/password/reset', null, { params })
      .subscribe({
        next: () => {
          this.message = 'Senha alterada com sucesso!';
          this.error = '';
        },
        error: () => {
          this.error = 'Erro ao redefinir a senha. Token pode estar expirado.';
          this.message = '';
        }
      });
  }
}
