import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts'; // L'URL de votre API pour les articles

  constructor(private http: HttpClient, private authService: AuthService) {}

  createPost(postData: any, subjectId: number): Observable<Post> {
    const token = this.authService.getToken();
    if (!token) {
      throw new Error('Token non trouvé - l’utilisateur n’est pas authentifié');
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    // Ajoutez subjectId comme paramètre dans l'URL
    return this.http.post<Post>(
      `${this.apiUrl}?subjectId=${subjectId}`,
      postData,
      { headers }
    );
  }
}
