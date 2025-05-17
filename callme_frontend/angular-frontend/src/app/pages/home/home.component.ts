import { Component } from '@angular/core';
import { FeedService } from "../../services/feed.service";
import { Router } from "@angular/router";
import { UsuariosComponent } from "../usuarios/usuarios.component";
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FeedItem } from "../../models/feed.models";
import {NgClass, NgForOf, NgIf} from "@angular/common";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    UsuariosComponent,
    NavbarComponent,
    NgForOf,
    NgIf,
    NgClass
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  isSideNavCollapsed: boolean = false;
  screenWidth: number = window.innerWidth;
  chamados: FeedItem[] = [];

  constructor(private feedService: FeedService, private router: Router) {}

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

  onToggleSideNav(event: SideNavToggle): void {
    this.isSideNavCollapsed = event.collapsed;
    this.screenWidth = event.screenWidth;
    console.log(' SideNav estado atualizado:', event);
  }

  navigateToChamado() {
    this.router.navigate(['/ChamadosInternos']);
  }

  navigateToCreate() {
    this.router.navigate(['/CriarChamados']);
  }
}
