import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/usuarios.models';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { NgClass, NgIf, NgForOf } from "@angular/common";
import { FormsModule } from "@angular/forms";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-userlisters',
  standalone: true,
  templateUrl: './userlisters.component.html',
  styleUrls: ['./userlisters.component.scss'],
  imports: [NavbarComponent, NgClass, NgIf, NgForOf, FormsModule]
})
export class UserlistersComponent implements OnInit {
  users: User[] = [];
  searchText: string = '';
  isSideNavCollapsed = false;
  screenWidth = window.innerWidth;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe({
      next: (res) => (this.users = res),
      error: (err) => console.error('Erro ao buscar usuários:', err),
    });
  }

  filteredUsers(): User[] {
    return this.users.filter((user) =>
      user.username.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }

  onToggleSideNav(event: SideNavToggle): void {
    this.isSideNavCollapsed = event.collapsed;
    this.screenWidth = event.screenWidth;
  }
}
