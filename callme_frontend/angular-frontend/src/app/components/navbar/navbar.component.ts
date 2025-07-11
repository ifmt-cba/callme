import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {RouterLink, RouterModule, Routes} from "@angular/router";
import {navbarData} from "./nav-data";
import {HomeComponent} from "../../pages/home/home.component";
import { AuthService } from "../../services/auth.service";
import {trigger} from "@angular/animations";

interface SideNavToggle{
  screenWidth: number;
  collapsed: boolean;
}
const routes: Routes = [
  {path : 'Dashboard', component: HomeComponent}

];

@Component({
  selector: 'app-navbar',
  standalone: true,

  imports: [
    NgForOf,
    NgIf,
    RouterLink,
    NgClass,
    NgForOf,
    RouterModule,

  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth = 0;
  navData = navbarData; // Mantemos os dados originais
  filteredNavData: any[] = [];
  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
    this.filterNavData();
  }

  filterNavData(): void {
    this.filteredNavData = this.navData.filter(item => {
      if (!item.role) {
        return true;
      }
      // Se o item tem a propriedade 'role'
      if (Array.isArray(item.role)) {
        // Se for um array, verifica se o usuário tem pelo menos uma das roles
        return item.role.some(role => this.authService.hasRole(role));
      } else {
        // Se for uma string, verifica se o usuário tem aquela role
        return this.authService.hasRole(item.role);
      }
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any): void {
    this.screenWidth = window.innerWidth;
    if (this.screenWidth <= 768) {
      this.collapsed = false;
      this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }
  }

  ToggleCollapsed(): void {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }

  CloseSidenav(): void {
    this.collapsed = false;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }
}
