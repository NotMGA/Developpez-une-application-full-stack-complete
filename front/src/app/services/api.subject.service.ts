import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; // AuthService est utilisé pour obtenir le token d'authentification

@Injectable({
  providedIn: 'root',
})
export class SubjectService {
  private apiUrl = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient, private authService: AuthService) {}

  getSubjects(): Observable<any[]> {
    const token = this.authService.getToken();
    if (!token) {
      throw new Error('Token non trouvé - l’utilisateur n’est pas authentifié');
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  subscribeToSubject(subjectId: number): Observable<any> {
    const token = this.authService.getToken();
    if (!token) {
      throw new Error('Token non trouvé - l’utilisateur n’est pas authentifié');
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    const url = `${this.apiUrl}/${subjectId}/subscribe`;
    return this.http.post(url, {}, { headers });
  }
  getUserSubscriptions(): Observable<any> {
    return this.http.get(`${this.apiUrl}/subscriptions`);
  }

  unsubscribeFromSubject(subjectId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${subjectId}/unsubscribe`);
  }
}
