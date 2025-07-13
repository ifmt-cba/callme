import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FeedResponse } from "../models/feed.models";
import {ChamadoUnificado} from "../models/chamado-unificado.model";


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
}
