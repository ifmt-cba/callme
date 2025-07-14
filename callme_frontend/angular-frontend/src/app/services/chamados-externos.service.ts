import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChamadoExterno } from '../models/chamado-externo.model'; // Modelo no singular

@Injectable({
  providedIn: 'root'
})
export class ChamadoExternoService {

  private apiUrl = 'http://localhost:8080/chamados';

  constructor(private http: HttpClient) { }

  // ✅ CORREÇÃO AQUI: O tipo de retorno deve ser 'ChamadoExterno[]' (singular)
  listarChamados(): Observable<ChamadoExterno[]> {
    return this.http.get<ChamadoExterno[]>(`${this.apiUrl}/listar`);
  }

  // (O resto dos seus métodos do serviço...)
  getMeusChamados(): Observable<ChamadoExterno[]> {
    return this.http.get<ChamadoExterno[]>(`${this.apiUrl}/meus-chamados`);
  }

}
