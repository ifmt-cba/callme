import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ChamadoResponse, ChamadosItem} from "../../models/Chamados.models";
import {ChamadosExternosService} from "../../services/chamados-externos.service";
@Component({
  selector: 'app-chamados-externos',
  standalone: true,
  imports: [CommonModule,],
  templateUrl: './chamados-externos.component.html',
  styleUrl: './chamados-externos.component.scss'
})
export class ChamadosExternosComponent implements OnInit {

  chamado: ChamadosItem[] = [];

  constructor(private chamadosExternosService: ChamadosExternosService) {}

  ngOnInit(): void {
    this.chamadosExternosService.getFeed().subscribe({
      next: (res) => {
        this.chamado = res.ChamadoItems;
      },
      error: (err) => {
        console.error('Erro ao carregar chamado:', err);
      }
    });
  }
}
