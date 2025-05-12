import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-forgot-password-layout',
  standalone: true,
  imports: [],
  templateUrl: './forgot-password-layout.component.html',
  styleUrl: './forgot-password-layout.component.scss'
})
export class ForgotPasswordLayoutComponent {
  @Input() primaryBtnText: string = "";
  @Input() title: string = "";
  @Input() secondaryBtnText: string = "";
  @Input() disablePrimaryBtm: boolean = true;
  @Output("submit") onSubmit = new EventEmitter();
  @Output("navigate") onNavigate = new EventEmitter();

  submit(){
    this.onSubmit.emit();
  }
  navigate(){
    this.onNavigate.emit();
  }

}
