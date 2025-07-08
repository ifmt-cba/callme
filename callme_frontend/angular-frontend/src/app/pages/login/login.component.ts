import { Component } from '@angular/core';
import {DefaultLoginLayoutComponent} from "../../components/default-login-layout/default-login-layout.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {PrimaryInputComponent} from "../../components/primary-input/primary-input.component";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {ToastrService} from "ngx-toastr";
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    DefaultLoginLayoutComponent,
    ReactiveFormsModule,
    PrimaryInputComponent
  ],
  providers: [
    LoginService
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loginForm!: FormGroup;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastService: ToastrService,
    private authService: AuthService
  ) {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  submit() {
    const { username, password } = this.loginForm.value;
    // Passando username e password como dois argumentos separados.
    this.loginService.login(username, password).subscribe({
      next: (response) => {
        console.log('Resposta da API de login:', response);

        if (response && response.token) {
          this.authService.setToken(response.token);
          this.toastService.success("Login realizado com sucesso!");
          this.router.navigate(["/home"]);
        } else {
          this.toastService.error("Resposta inesperada do servidor.");
          console.error("A resposta da API não contém a propriedade 'token'.", response);
        }
      },
      error: (err) => {
        console.error("Erro na chamada de login:", err);
        // Exemplo de como tratar diferentes erros de status
        if (err.status === 401 || err.status === 403) {
          this.toastService.error("Usuário ou senha inválidos!");
        } else {
          this.toastService.error("Ocorreu um erro. Tente novamente mais tarde.");
        }
      }
    });
  }

  navigate(){
    this.router.navigate(["signup"]);
  }

  navigateToReset() {
    this.router.navigate(['/resetsenha']);
  }
}
