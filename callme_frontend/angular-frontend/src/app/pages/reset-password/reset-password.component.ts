import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import {ToastrService} from "ngx-toastr";

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
    private http: HttpClient,
    private toastService: ToastrService,
    private router: Router,
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
          this.toastService.success("Senha Trocada com Sucesso")
          setTimeout(() => {this.router.navigate(["/principal"]);
          },1000)
        },
        error: () => this.toastService.error("Token inválido ou expirado "),

      })
  }
  navigateToLogin(): void {
    this.router.navigate(['/principal']);
  }
}
