
// src/app/services/chamados-externos.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ChamadoResponse, ChamadosItem} from '../models/Chamados.models';

@Injectable({
  providedIn: 'root'
})
export class ChamadosExternosService {

  private apiUrl = 'http://localhost:8080/chamados/listar';

  constructor(private http: HttpClient) {}

  getFeed(): Observable<ChamadosItem[]> {
    return this.http.get<ChamadosItem[]>(this.apiUrl);
  }

  getChamadoById(id: string): Observable<ChamadosItem> {
    return this.http.get<ChamadosItem>(`/api/chamados/${id}`);
  }

  updateChamado(chamado: ChamadosItem): Observable<any> {
    return this.http.put(`/api/chamados/${chamado.tokenEmail}`, chamado);
  }

  deleteChamado(tokenEmail: string): Observable<any> {
    return this.http.delete(`/api/chamados/${tokenEmail}`);
  }
}
