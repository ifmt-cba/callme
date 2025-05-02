import { Component } from '@angular/core';
import {DefaultLoginLayoutComponent} from "../../components/default-login-layout/default-login-layout.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {PrimaryInputComponent} from "../../components/primary-input/primary-input.component";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {ToastrService} from "ngx-toastr";

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

  constructor(private router: Router,
  private loginService: LoginService, private toastService: ToastrService
  ) {

    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    })
  }

  submit() {
   this.loginService.login(this.loginForm.value.username, this.loginForm.value.password).subscribe({
     next: () => {
       this.toastService.success("Login successfull")
       setTimeout(() => {this.router.navigate(["/home"]);
         },1000)
     },
     error: () => this.toastService.error("Senha ou usuario incorretos "),

   })
  }

  navigate(){
    this.router.navigate(["signup"]);
  }

}
