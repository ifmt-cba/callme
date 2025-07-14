import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChamadoInternoPayload {
  content: string;
  descricao: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChamadoInternoService {
  private apiUrl = 'http://localhost:8080/chamados'; // URL do endpoint que você criou no backend

  constructor(private http: HttpClient) { }

  criarChamado(payload: ChamadoInternoPayload): Observable<any> {
    return this.http.post(this.apiUrl, payload);
  }
}
