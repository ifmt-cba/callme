import { Component } from '@angular/core';
import {FeedService} from "../../services/feed.service";
import {Router} from "@angular/router";
import {UsuariosComponent} from "../usuarios/usuarios.component";
import {NavbarComponent} from "../../components/navbar/navbar.component";
import {FeedItem} from "../../models/feed.models";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    UsuariosComponent,
    NavbarComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  chamados: FeedItem[] = [];

  constructor(private feedService: FeedService, private router: Router) {

  }
  ngOnInit(): void {
    this.feedService.getFeed().subscribe({
      next: (res) => {
        this.chamados = res.feedItens;
      },
      error: (err) => {
        console.error('Erro ao carregar chamados:', err);
      }
    });
  }
  navigateToChamado(){
    this.router.navigate(['/ChamadosInternos']);
  }
  navigateToCreate(){
    this.router.navigate(['/CriarChamados']);
  }
}
