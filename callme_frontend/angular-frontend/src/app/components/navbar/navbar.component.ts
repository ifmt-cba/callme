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
  ngOnInit() {
    this.screenWidth = window.innerWidth;

  }
  constructor(public authService: AuthService) {}

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.screenWidth = window.innerWidth;
    if(this.screenWidth <= 768){
      this.collapsed = false;
      this.onToggleSideNav.emit({collapsed:this.collapsed, screenWidth: this.screenWidth});

    }
  }

  @Output()onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth =0;
  navData = navbarData
  filteredNavData: any[] = [];

  ToggleCollapsed() {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({collapsed:this.collapsed, screenWidth: this.screenWidth});
  }



  filterNavData(): void {
    this.filteredNavData = this.navData.filter(item => {
      if (!item.role) {
        return true;
      }
      if (Array.isArray(item.role)) {
        return item.role.some(role => this.authService.hasRole(role));
      } else {
        return this.authService.hasRole(item.role);
      }
    });
  }

  CloseSidenav() {
    this.collapsed = false;
    this.onToggleSideNav.emit({collapsed:this.collapsed, screenWidth: this.screenWidth});

  }



}
