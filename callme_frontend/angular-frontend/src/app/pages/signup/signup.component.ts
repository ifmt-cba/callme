import { Component } from '@angular/core';
import {DefaultLoginLayoutComponent} from "../../components/default-login-layout/default-login-layout.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {PrimaryInputComponent} from "../../components/primary-input/primary-input.component";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {ToastrService} from "ngx-toastr";

interface SignupForm {
  username: FormControl,
  password: FormControl,
  email: FormControl,

}
@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    DefaultLoginLayoutComponent,
    ReactiveFormsModule,
    PrimaryInputComponent
  ],
  providers: [
    LoginService
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signupForm!: FormGroup<SignupForm>;

  constructor(private router: Router,
              private loginService: LoginService,
              private toastService: ToastrService
  ) {

    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required),

    })
  }

  submit() {
   this.loginService.signup(this.signupForm.value.username, this.signupForm.value.password, this.signupForm.value.email).subscribe({
     next: () => {
       this.toastService.success("Usuario Criado com sucesso")
       setTimeout(() => {this.router.navigate(["/login"]);
       },1200)
     },
     error: () => this.toastService.error("Tente se registrar mais tarde  "),
   })
  }

  navigate(){
    this.router.navigate(["login"]);
  }

}
