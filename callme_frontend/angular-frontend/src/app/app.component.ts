import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {UsuariosComponent} from "./pages/usuarios/usuarios.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, UsuariosComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'angular-frontend';
}
