import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChamadosExternosService } from '../../services/chamados-externos.service';
import { ChamadosItem, ChamadoResponse } from '../../models/Chamados.models';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule, NavbarComponent, HttpClientModule],
  templateUrl: './chamados-externos.component.html',
  styleUrls: ['./chamados-externos.component.scss']
})
export class ChamadosExternosComponent implements OnInit {
  chamado: ChamadosItem[] = [];

  constructor(private chamadosExternosService: ChamadosExternosService) {}

  ngOnInit(): void {
    this.chamadosExternosService.getFeed().subscribe({
      next: (res: ChamadosItem[]) => {
        this.chamado = res;
      },
      error: (err) => {
        console.error('Erro ao carregar chamados:', err);
        this.chamado = [];
      }
    });
  }
}
