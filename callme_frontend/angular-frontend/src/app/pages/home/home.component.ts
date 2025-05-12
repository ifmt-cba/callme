import { Component } from '@angular/core';
import {FeedService} from "../../services/feed.service";
import {Router} from "@angular/router";
import {UsuariosComponent} from "../usuarios/usuarios.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    UsuariosComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(private router: Router) {


  }

  navigateToChamado(){
    this.router.navigate(['/ChamadoInternos']);
  }
}
