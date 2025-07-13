import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/usuarios.models';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FormsModule } from "@angular/forms";
import { NgClass, NgIf, NgForOf } from "@angular/common";
import { UserCreateModalComponent } from '../../components/user-create-modal/user-create-modal.component';
import { ToastrService } from 'ngx-toastr'; // 1. Importe o ToastrService

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
  currentUserId: string | null = null;

  dropdownOpen = false;
  selectedRole: string | null = null;
  showModal = false;
  editingUser: User | null = null;

  // 2. Injete o ToastrService no construtor
  constructor(private userService: UserService, private toastr: ToastrService) {
    this.currentUserId = this.userService.getCurrentUserId();
  }

  ngOnInit(): void {
    this.userService.getUsers().subscribe({
      next: (res) => (this.users = res),
      error: (err) => this.toastr.error('Falha ao carregar a lista de usuários.', 'Erro'),
    });
  }

  get filteredUsers(): User[] {
    if (!this.searchText) {
      return this.users;
    }
    return this.users.filter((user) =>
      user.username?.toLowerCase().includes(this.searchText.toLowerCase())
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

    if (this.editingUser) { // Lógica de EDIÇÃO
      const userPayload: User = {
        id: this.editingUser.id,
        username: userData.username,
        email: userData.email,
        roles: this.editingUser.roles,
        password: userData.password
      };

      this.userService.updateUser(userPayload).subscribe({
        next: (updatedUser) => {
          this.closeModal();
          // 3. Adicione o toast verde de sucesso
          this.toastr.success('Usuário atualizado com sucesso!', 'Sucesso');
          // 4. Recarregue a página após um curto intervalo
          setTimeout(() => window.location.reload(), 300);
        },
        error: (err) => this.toastr.error('Erro ao atualizar usuário.', 'Erro'),
      });
    } else { // Lógica de CRIAÇÃO
      const userPayload = {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        roles: [this.selectedRole]
      };

      this.userService.createUser(userPayload, this.selectedRole).subscribe({
        next: (createdUser) => {
          this.closeModal();
          // 3. Adicione o toast verde de sucesso
          this.toastr.success('Usuário criado com sucesso!', 'Sucesso');
          // 4. Recarregue a página
          setTimeout(() => window.location.reload(), 300);
        },
        error: (err) => this.toastr.error('Erro ao criar usuário.', 'Erro'),
      });
    }
  }

  deleteUser(userId: string): void {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          // 3. Adicione o toast azul de informação
          this.toastr.info('Usuário excluído com sucesso.', 'Excluído');
          // 4. Recarregue a página
          setTimeout(() => window.location.reload(), 300);
        },
        error: (err) => {
          if (err.status === 403) {
            this.toastr.warning('Não é permitido excluir seu próprio usuário.', 'Aviso');
          } else {
            this.toastr.error('Erro ao deletar usuário.', 'Erro');
          }
        },
      });
    }
  }

  editUser(user: User): void {
    this.selectedRole = user.roles[0];
    this.editingUser = { ...user };
    this.showModal = true;
  }

  isCurrentUser(userId: string): boolean {
    return userId === this.currentUserId;
  }
}
