import { Component, ElementRef, ViewChild, Renderer2 } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

interface  LoginResponse{
  acessToken: string;
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
              private toastService: ToastrService) {
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
  submit() {
    this.loginService.signup(this.signupForm.value.username, this.signupForm.value.password, this.signupForm.value.email).subscribe({
      next: () => {
        this.toastService.success("Usuario Criado com sucesso")
        setTimeout(() => {this.router.navigate(["/usuarios"]);
        },1200)
      },
      error: () => this.toastService.error("Tente se registrar mais tarde  "),
    })
  }
  navigate(){
    this.router.navigate(["login"]);
  }
  // Em usuarios.component.ts

  submitLogin() {
    this.loginService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe({
      next: (res: any) => {
        // Garanta que a resposta do backend tem a chave "accessToken"
        const token = res.acessToken;

        if (token) {
          // Salve no sessionStorage com a chave padrão "accessToken"
          sessionStorage.setItem('acessToken', token);

          this.toastService.success("Login realizado com sucesso!");
          this.router.navigate(["/home"]);
        } else {
          console.error("A resposta da API de login não contém a propriedade 'acessToken'.", res);
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
}
