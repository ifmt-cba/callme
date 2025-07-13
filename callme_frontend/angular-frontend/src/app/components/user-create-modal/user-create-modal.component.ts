import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/usuarios.models';
// Import NgIf to use it in the template
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-user-create-modal',
  standalone: true,
  templateUrl: './user-create-modal.component.html',
  styleUrls: ['./user-create-modal.component.scss'],
  // Add NgIf to the imports array
  imports: [FormsModule, NgIf],
})
export class UserCreateModalComponent implements OnInit {
  // ✅ CORREÇÃO: Ensure these @Input decorators exist.
  @Input() editingUser: User | null = null;
  @Input() selectedRole: string | null = null;

  // ✅ CORREÇÃO: The @Output must be named 'submitUser' and be correctly typed.
  @Output() submitUser = new EventEmitter<{ username: string; email: string; password: string }>();
  @Output() close = new EventEmitter<void>();

  // Internal component state
  username = '';
  email = '';
  password = '';
  isEditing = false;

  ngOnInit() {
    if (this.editingUser) {
      this.username = this.editingUser.username || '';
      this.email = this.editingUser.email;
      this.isEditing = true;
    }
  }

  submitForm() {
    // This now emits the correctly named and typed event.
    this.submitUser.emit({
      username: this.username,
      email: this.email,
      password: this.password,
    });
  }
}
