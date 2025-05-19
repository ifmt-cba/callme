import { Component, ElementRef, ViewChild, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent {
  @ViewChild('container', { static: true }) containerRef!: ElementRef;

  constructor(private renderer: Renderer2) {}

  onSignUp(): void {
    this.renderer.addClass(this.containerRef.nativeElement, 'sign-up-mode');
  }

  onSignIn(): void {
    this.renderer.removeClass(this.containerRef.nativeElement, 'sign-up-mode');
  }
}
