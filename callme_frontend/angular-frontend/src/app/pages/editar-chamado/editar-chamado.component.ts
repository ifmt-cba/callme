import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {FormsModule} from "@angular/forms";
import {NavbarComponent} from "../../components/navbar/navbar.component";

@Component({
  selector: 'app-editar-chamado',
  templateUrl: './editar-chamado.component.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarComponent
  ],
  styleUrls: ['./editar-chamado.component.scss']
})
export class EditarChamadoComponent implements OnInit {
  chamado: any;
  tokenEmail!: string;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.tokenEmail = this.route.snapshot.paramMap.get('tokenEmail')!;
    console.log('Token recebido:', this.tokenEmail); // Verifique se está chegando

    this.http.get(`http://localhost:8080/chamados/buscar/${this.tokenEmail}`)
      .subscribe({
        next: (data) => {
          console.log('Chamado recebido:', data);
          this.chamado = data;
        },
        error: (err) => {
          console.error('Erro ao buscar chamado:', err);
          alert('Erro ao carregar os dados do chamado.');
        }
      });
  }


  buscarChamado(): void {
    this.http.get(`http://localhost:8080/chamados/buscar/${this.tokenEmail}`)
      .subscribe((data) => {
        this.chamado = data;
      }, (error) => {
        console.error('Erro ao buscar chamado:', error);
      });
  }

  salvar(): void {
    // Apenas esta linha muda: enviamos o objeto 'chamado' inteiro.
    this.http.put(`http://localhost:8080/chamados/editar/token/${this.tokenEmail}`, this.chamado)
      .subscribe({
        next: () => {
          alert('Chamado atualizado com sucesso!');
          this.router.navigate(['/chamados']); // Sugestão: redirecionar para a lista de chamados
        },
        error: (error) => {
          console.error('Erro ao salvar chamado:', error);
          alert('Erro ao salvar chamado.');
        }
      });
  }




  excluir(): void {
    if (confirm('Tem certeza que deseja excluir este chamado?')) {
      this.http.delete(`http://localhost:8080/chamados/${this.chamado.id}`)
        .subscribe(() => {
          alert('Chamado excluído com sucesso!');
          this.router.navigate(['/']);
        }, error => {
          console.error('Erro ao excluir chamado:', error);
          alert('Erro ao excluir.');
        });
    }
  }

}
