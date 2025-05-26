import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-create-modal',
  standalone: true,
  templateUrl: './user-create-modal.component.html',
  styleUrls: ['./user-create-modal.component.scss'],
  imports: [FormsModule],
})
export class UserCreateModalComponent {
  @Input() role: string = '';
  @Output() create = new EventEmitter<{ username: string; email: string; password: string }>();
  @Output() close = new EventEmitter<void>();

  username = '';
  email = '';
  password = '';

  submitForm() {
    this.create.emit({ username: this.username, email: this.email, password: this.password });
  }
}
