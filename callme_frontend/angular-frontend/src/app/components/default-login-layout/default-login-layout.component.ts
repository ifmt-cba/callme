import {Component, EventEmitter, Input, input, Output} from '@angular/core';
@Component({
  selector: 'app-default-login-layout',
  standalone: true,
  imports: [],
  templateUrl: './default-login-layout.component.html',
  styleUrl: './default-login-layout.component.scss'
})
export class DefaultLoginLayoutComponent {

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
