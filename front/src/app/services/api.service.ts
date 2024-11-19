import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../pages/login/login-response.interface';
import { AuthService } from './auth.service'; // Pour récupérer le jeton

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private createAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/register`, user);
  }

  login(credentials: {
    email: string;
    password: string;
  }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/users/login`,
      credentials
    );
  }

  getProfile(userId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get(`${this.apiUrl}/users/${userId}/profile`, { headers });
  }

  getFeed(userId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get(`${this.apiUrl}/posts/feed?userId=${userId}`, {
      headers,
    });
  }

  getPostById(postId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get<any>(`${this.apiUrl}/posts/${postId}`, { headers });
  }
  // Méthode pour récupérer les commentaires d'un article
  getCommentsByPost(postId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/comments/post/${postId}`);
  }
}
