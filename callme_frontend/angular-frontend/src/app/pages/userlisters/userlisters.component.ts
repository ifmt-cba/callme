import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/usuarios.models';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FormsModule } from "@angular/forms";
import { NgClass, NgIf, NgForOf } from "@angular/common";
import { UserCreateModalComponent } from '../../components/user-create-modal/user-create-modal.component';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-userlisters',
  standalone: true,
  templateUrl: './userlisters.component.html',
  styleUrls: ['./userlisters.component.scss'],
  imports: [NavbarComponent, NgClass, NgIf, NgForOf, FormsModule, UserCreateModalComponent]
})
export class UserlistersComponent implements OnInit {
  users: User[] = [];
  searchText: string = '';
  isSideNavCollapsed = false;
  screenWidth = window.innerWidth;

  dropdownOpen = false;
  selectedRole: string | null = null;
  showModal = false;
  editingUser: User | null = null;

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

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  openCreateModal(role: string): void {
    this.selectedRole = role;
    this.dropdownOpen = false;
    this.showModal = true;
    this.editingUser = null;
  }

  closeModal(): void {
    this.showModal = false;
    this.selectedRole = null;
    this.editingUser = null;
  }

  createUser(userData: { username: string; email: string; password: string }): void {
    if (!this.selectedRole) return;

    if (this.editingUser) {
      const userPayload: User = {
        id: this.editingUser.id,
        username: userData.username,
        email: userData.email,
        roles: this.editingUser.roles,
        password: userData.password
      };

      this.userService.updateUser(userPayload).subscribe({
        next: (updatedUser) => {
          const index = this.users.findIndex(u => u.id === userPayload.id);
          if (index !== -1) this.users[index] = updatedUser;
          this.closeModal();
        },
        error: (err) => {
          console.error('Erro ao editar usuário:', err);
          alert('Erro ao editar usuário. Por favor, tente novamente.');
        },
      });
    } else {
      const userPayload = {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        roles: [this.selectedRole]
      };

      this.userService.createUser(userPayload, this.selectedRole).subscribe({
        next: (createdUser) => {
          this.users.push(createdUser);
          this.closeModal();
        },
        error: (err) => {
          console.error('Erro ao criar usuário:', err);
          alert('Erro ao criar usuário. Por favor, tente novamente.');
        },
      });
    }
  }

  deleteUser(userId: string): void {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => this.users = this.users.filter(u => u.id !== userId),
        error: (err) => console.error('Erro ao deletar usuário:', err),
      });
    }
  }

  editUser(user: User): void {
    this.selectedRole = user.roles[0];
    this.editingUser = { ...user };
    this.showModal = true;
  }
}
