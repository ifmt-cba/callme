import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChamadosExternosService } from '../../services/chamados-externos.service';
import { ChamadosItem } from '../../models/Chamados.models';
import {FormsModule} from "@angular/forms";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-editar-chamado',
  templateUrl: './editar-chamado.component.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  styleUrls: ['./editar-chamado.component.scss']
})
export class EditarChamadoComponent implements OnInit {

  chamado!: ChamadosItem;
  tecnico: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private chamadosService: ChamadosExternosService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log('Token recebido na rota:', id);
    if (id) {
      this.chamadosService.getChamadoById(id).subscribe((res) => {
        console.log('Chamado carregado:', res);
        this.chamado = res;
      });
    }
  }

  salvar() {
    this.chamadosService.updateChamado(this.chamado).subscribe(() => {
      alert('Chamado atualizado com sucesso!');
      this.router.navigate(['/']);
    });
  }

  excluir() {
    if (confirm('Tem certeza que deseja excluir este chamado?')) {
      this.chamadosService.deleteChamado(this.chamado.tokenEmail).subscribe(() => {
        alert('Chamado excluído com sucesso!');
        this.router.navigate(['/']);
      });
    }
  }
}
