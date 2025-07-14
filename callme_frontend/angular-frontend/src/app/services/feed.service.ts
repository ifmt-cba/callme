import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FeedResponse } from "../models/feed.models";
import {ChamadoUnificado} from "../models/chamado-unificado.model";
import {Acompanhamento} from "../models/acompanhamento.model";


@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getFeed(): Observable<FeedResponse> {
    return this.http.get<FeedResponse>('{this.apiUrl}/feed');
  }
  getChamadosUnificados(): Observable<ChamadoUnificado[]> {
    return this.http.get<ChamadoUnificado[]>(`${this.apiUrl}/chamados-unificados`);
  }
  getAcompanhamentoPorToken(token: string): Observable<Acompanhamento> {
    return this.http.get<Acompanhamento>(`${this.apiUrl}/acompanhamentos/${token}`);
  }
}
