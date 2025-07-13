import { Component } from '@angular/core';
import { FeedService } from "../../services/feed.service";
import { Router } from "@angular/router";
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FeedItem } from "../../models/feed.models";
import {DatePipe, NgClass, NgForOf, NgIf, SlicePipe} from "@angular/common";
import {ChamadoUnificado} from "../../models/chamado-unificado.model";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    NgForOf,
    NgIf,
    NgClass,
    SlicePipe,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  isSideNavCollapsed: boolean = false;
  screenWidth: number = window.innerWidth;
  chamados: FeedItem[] = [];
  chamadosUnificados: ChamadoUnificado[] = [];

  constructor(private feedService: FeedService, private router: Router) {}

  ngOnInit(): void {
    // Chamar o novo método ao iniciar o componente
    this.feedService.getChamadosUnificados().subscribe({
      next: (res) => {
        this.chamadosUnificados = res;
        console.log('Chamados unificados recebidos:', this.chamadosUnificados);
      },
      error: (err) => {
        console.error('Erro ao carregar chamados unificados:', err);
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
