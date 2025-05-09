import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FeedResponse } from "../models/feed.models";


@Injectable({
  providedIn: 'root'
})
export class FeedService {
  private apiUrl = 'http://localhost:8080/feed';

  constructor(private http: HttpClient) {}

  getFeed(): Observable<FeedResponse> {
    return this.http.get<FeedResponse>(this.apiUrl);
  }
}
