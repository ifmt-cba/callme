import { Component, ElementRef, ViewChild, Renderer2 } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

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

  submitLogin() {
    this.loginService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe({
      next: () => {
        this.toastService.success("Login successfull")
        setTimeout(() => {this.router.navigate(["/home"]);
        },1000)
      },
      error: () => this.toastService.error("Senha ou usuario incorretos "),

    })
  }
  navigateTosup(){
    this.router.navigate(["signup"]);
  }
  navigateToReset() {
    this.router.navigate(['/resetsenha']);
  }

}
