import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart, ChartConfiguration, ChartData, ChartType, registerables } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { NgIf, NgClass } from '@angular/common';
import { NavbarComponent } from "../../components/navbar/navbar.component";

Chart.register(...registerables);

// Interfaces
interface SideNavToggle { collapsed: boolean; screenWidth: number; }
interface ChamadoStatusDTO { abertos: number; andamentos: number; fechads: number; }

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ BaseChartDirective, NgIf, NgClass, NavbarComponent ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  isSideNavCollapsed = false;
  screenWidth = 0;
  isLoading = true;

  // Propriedades para os totais
  totalChamadosStatus: number = 0;

  // --- Gráfico de Pizza (Status) ---
  public pieChartOptions: ChartConfiguration['options'] = { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: true, position: 'bottom' } } };
  public pieChartData: ChartData<'pie'> = {
    labels: [ 'Abertos', 'Em Andamento', 'Fechados' ],
    datasets: [{
      data: [ 0, 0, 0 ],
      backgroundColor: ['#f44336', '#ffc107', '#4caf50'],
    }]
  };
  public pieChartType: ChartType = 'pie';

  // --- Gráfico de Barras (mesmos dados do Pizza) ---
  public barChartOptions: ChartConfiguration['options'] = { responsive: true, maintainAspectRatio: false, scales: { x: {}, y: { min: 0 } }, plugins: { legend: { display: false } } };
  public barChartType: ChartType = 'bar';
  public barChartData: ChartData<'bar'> = {
    labels: [ 'Abertos', 'Em Andamento', 'Fechados' ], // Mesmos labels
    datasets: [
      {
        data: [ 0, 0, 0 ], // Mesmos dados
        label: 'Status dos Chamados',
        backgroundColor: ['#f44336', '#ffc107', '#4caf50'],
      }
    ]
  };

  // --- NOVO: Gráfico de Linha (Chamados por Dia) ---
  public lineChartOptions: ChartConfiguration['options'] = { responsive: true, maintainAspectRatio: false, scales: { x: {}, y: { min: 0 } }, plugins: { legend: { display: false } } };
  public lineChartType: ChartType = 'line';
  public lineChartData: ChartData<'line'> = {
    labels: [ 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom' ],
    datasets: [
      {
        data: [ 0, 0, 0, 0, 0, 0, 0 ], // Inicia zerado
        label: 'Chamados Criados',
        fill: true,
        tension: 0.4,
        borderColor: 'rgba(75,192,192,1)',
        backgroundColor: 'rgba(75,192,192,0.2)'
      }
    ]
  };


  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const statusApiUrl = 'http://localhost:8080/api/dashboard/chamado-stats';

    this.http.get<ChamadoStatusDTO>(statusApiUrl).subscribe({
      next: (stats) => {
        const statusData = [stats.abertos, stats.andamentos, stats.fechads];

        // Alimenta ambos os gráficos (Pizza e Barras)
        this.pieChartData.datasets[0].data = statusData;
        this.barChartData.datasets[0].data = statusData;

        // Calcula o total para o card
        this.totalChamadosStatus = statusData.reduce((a, b) => a + b, 0);
      },
      error: (err) => console.error('Erro ao buscar status', err)
    });

    // SIMULAÇÃO: Dados para o gráfico de linha
    setTimeout(() => {
      this.lineChartData.datasets[0].data = [ 5, 9, 7, 8, 6, 5, 10 ];
      this.isLoading = false; // Termina o carregamento
    }, 500);
  }

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
}
