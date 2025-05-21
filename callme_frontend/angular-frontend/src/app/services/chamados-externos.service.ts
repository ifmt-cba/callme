import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FeedResponse} from "../models/feed.models";
import {data} from "../models/Chamados.models";

@Injectable({
  providedIn: 'root'
})
export class ChamadosExternosService {

  private apiUrl = 'http://localhost:8080/chamados/abrir';

  constructor(private http: HttpClient ) {}

    getFeed(): Observable<ChamadosResponse> {
      return this.http.get<ChamadosResponse>(this.apiUrl);
    }

}
