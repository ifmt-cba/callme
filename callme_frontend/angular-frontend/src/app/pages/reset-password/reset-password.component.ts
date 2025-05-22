import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  styleUrls:  ['./reset-password.component.scss'],
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  token: string = '';
  message: string = '';
  error: string = '';

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient
  ) {
    this.form = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

  onSubmit(): void {
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
        error: (err) => {
          this.error = 'Erro ao redefinir a senha. Token pode estar expirado.';
          this.message = '';
        }
      });
  }
}
