import {Component, Input} from '@angular/core';
import {DefaultLoginLayoutComponent} from "../../components/default-login-layout/default-login-layout.component";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {PrimaryInputComponent} from "../../components/primary-input/primary-input.component";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {ToastrService} from "ngx-toastr";
import {ForgotPasswordLayoutComponent} from "../../components/forgot-password-layout/forgot-password-layout.component";

interface resetForm {
  email: FormControl,

}
@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    DefaultLoginLayoutComponent,
    ReactiveFormsModule,
    PrimaryInputComponent,
    ForgotPasswordLayoutComponent,
  ],
  providers: [
    LoginService
  ],
  templateUrl: './reset-senha.html',
  styleUrl: './reset-senha.scss'
})

export class ResetSenhaComponent {

  resetForm!: FormGroup<resetForm>;

  constructor(private router: Router,
              private loginService: LoginService,
              private toastService: ToastrService
  ) {

    this.resetForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),

    })
  }

  submit() {
   this.loginService.resetsenha( this.resetForm.value.email).subscribe({
     next: () => {
       this.toastService.success("Usuario Criado com sucesso")
       setTimeout(() => {this.router.navigate(["/login"]);
       },1200)
     },
     error: () => this.toastService.error("Tente se registrar mais tarde  "),
   })
  }
  navigate(){
    this.router.navigate(["/principal"]);
  }

}
