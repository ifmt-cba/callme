import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FeedResponse} from "../models/feed.models";
import {ChamadoResponse} from "../models/Chamados.models";

@Injectable({
  providedIn: 'root'
})
export class ChamadosExternosService {

  private apiUrl = 'http://localhost:8080/chamados';

  constructor(private http: HttpClient ) {}

    getFeed(): Observable<ChamadoResponse> {
      return this.http.get<ChamadoResponse>(this.apiUrl);
    }

}
