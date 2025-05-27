import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/usuarios.models';

@Component({
  selector: 'app-user-create-modal',
  standalone: true,
  templateUrl: './user-create-modal.component.html',
  styleUrls: ['./user-create-modal.component.scss'],
  imports: [FormsModule],
})
export class UserCreateModalComponent implements OnInit {
  @Input() role: string = '';
  @Input() editingUser: User | null = null;
  @Output() create = new EventEmitter<{ username: string; email: string; password: string }>();
  @Output() close = new EventEmitter<void>();

  username = '';
  email = '';
  password = '';
  isEditing = false;

  ngOnInit() {
    if (this.editingUser) {
      this.username = this.editingUser.username;
      this.email = this.editingUser.email;
      this.isEditing = true;
    }
  }

  submitForm() {
    this.create.emit({
      username: this.username,
      email: this.email,
      password: this.password
    });
  }
}
