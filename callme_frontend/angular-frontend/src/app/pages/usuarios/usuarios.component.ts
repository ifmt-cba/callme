import { Component, ElementRef, ViewChild, Renderer2 } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import { AuthService } from '../../services/auth.service';

interface  LoginResponse{
  accessToken: string;
}
interface SignupForm {
  username: FormControl,
  password: FormControl,
  email: FormControl,

}
@Component({
  selector: 'app-usuarios',
  standalone: true,
  providers:[
    LoginService
  ],
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent {

  signupForm!: FormGroup<SignupForm>;
  loginForm!: FormGroup;
  @ViewChild('container', { static: true }) containerRef!: ElementRef;
  constructor(private renderer: Renderer2, private loginService : LoginService, private router: Router,
              private toastService: ToastrService, private authService : AuthService) {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    })
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
    })
  }

  onSignUp(): void {
    this.renderer.addClass(this.containerRef.nativeElement, 'sign-up-mode');
  }

  onSignIn(): void {
    this.renderer.removeClass(this.containerRef.nativeElement, 'sign-up-mode');
  }

  // ✅ FUNÇÃO CORRIGIDA
  submit() {
    this.loginService.signup(this.signupForm.value.username, this.signupForm.value.password, this.signupForm.value.email).subscribe({
      next: () => {
        this.toastService.success("Usuario Criado com sucesso");

        // Em vez de navegar, chamamos a função que volta para a tela de login
        setTimeout(() => {
          this.onSignIn();
        }, 1200); // 1.2 segundos para o usuário ler a mensagem
      },
      error: () => this.toastService.error("Tente se registrar mais tarde"),
    })
  }

  navigate(){
    this.router.navigate(["login"]);
  }

  submitLogin() {
    this.loginService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe({
      next: (res: any) => {
        const token = res.accessToken;

        if (token) {
          this.authService.setToken(token);
          this.toastService.success("Login realizado com sucesso!");
          this.router.navigate(["/home"]);
        } else {
          console.error("A resposta da API de login não contém a propriedade 'accessToken'.", res);
          this.toastService.error("Resposta de login inesperada.");
        }
      },
      error: () => this.toastService.error("Senha ou usuário incorretos"),
    });
  }

  navigateTosup(){
    this.router.navigate(["signup"]);
  }

  navigateToReset() {
    this.router.navigate(['/resetsenha']);
  }

  navigateToAcompanhamentos(): void {
    this.router.navigate(['/acompanhamentos']);
  }
}
