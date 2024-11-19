import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getUserProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/profile`);
  }

  updateUserProfile(user: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/profile`, user);
  }
}
