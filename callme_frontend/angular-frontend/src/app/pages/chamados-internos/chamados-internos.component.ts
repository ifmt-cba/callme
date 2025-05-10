import { Component } from '@angular/core';
import {FeedService} from "../../services/feed.service";
import {FeedItem} from "../../models/feed.models";
import {CommonModule} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-chamados-internos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chamados-internos.component.html',
  styleUrl: './chamados-internos.component.scss'
})
export class ChamadosInternosComponent {

  chamados: FeedItem[] = [];

  constructor(private feedService: FeedService, private router: Router) {

  }

  ngOnInit(): void {
    this.feedService.getFeed().subscribe({
      next: (res) => {
        this.chamados = res.feedItens;
      },
      error: (err) => {
        console.error('Erro ao carregar chamados:', err);
      }
    });
  }

  navigateToHome(){

    this.router.navigate(["/home"]);
  }

}
