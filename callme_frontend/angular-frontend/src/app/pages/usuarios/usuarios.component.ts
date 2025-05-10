import { Component } from '@angular/core';
import {navbarData} from "./nav-data";
import {RouterLink} from "@angular/router";
import {NgClass, NgIf,NgForOf } from "@angular/common";


@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    RouterLink,
    NgClass,
    NgIf,
    NgForOf
  ],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent {

  collapsed = true;
  navData = navbarData;

}
