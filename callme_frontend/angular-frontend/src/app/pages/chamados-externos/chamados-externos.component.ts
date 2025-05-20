import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';  // para usar ngFor e outras diretivas
import { ChamadosExternosService } from '../../services/chamados-externos.service';
import { ChamadosItem } from '../../models/Chamados.models';

@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule],  // diretivas básicas do Angular
  templateUrl: './chamados-externos.component.html',
  styleUrls: ['./chamados-externos.component.scss']
})

export class ChamadosExternosComponent implements OnInit {

  chamado: ChamadosItem[] = [];

  constructor(private chamadosExternosService: ChamadosExternosService) {}

  ngOnInit(): void {
    this.chamadosExternosService.getFeed().subscribe({
      next: (res) => {
        this.chamado = res.ChamadoItems;  // verifique o nome do array que seu backend retorna
      },
      error: (err) => {
        console.error('Erro ao carregar chamado:', err);
      }
    });
  }
}
