import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChamadosExternosService } from '../../services/chamados-externos.service';
import { ChamadosItem, ChamadosResponse } from '../../models/Chamados.models';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './chamados-externos.component.html',
  styleUrls: ['./chamados-externos.component.scss']
})
export class ChamadosExternosComponent implements OnInit {

  chamado: ChamadosItem[] = [];

  constructor(private chamadosExternosService: ChamadosExternosService) {}

  ngOnInit(): void {
    this.chamadosExternosService.getFeed().subscribe({
      next: (res: ChamadosResponse) => {
        this.chamado = res.data;
        console.log('Chamados carregados:', this.chamado);
      },
      error: (err) => {
        console.error('Erro ao carregar chamados:', err);
      }
    });
  }
}
