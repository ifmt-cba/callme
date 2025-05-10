import { Component } from '@angular/core';
import {FeedService} from "../../services/feed.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(private router: Router) {

    
  }

  navigateToChamado(){
    this.router.navigate(['/ChamadoInternos']);
  }
}
