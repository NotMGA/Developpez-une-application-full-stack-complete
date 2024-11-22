import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient, private authService: AuthService) {}

  /**
   * Creates a new post and associates it with a specific subject.
   * @param postData The data of the post to be created (e.g., title, content).
   * @param subjectId The ID of the subject to associate the post with.
   * @returns An `Observable` of the newly created post.
   * @throws An error if the user is not authenticated (token is missing).
   */
  createPost(postData: any, subjectId: number): Observable<Post> {
    // Retrieve the authentication token from the AuthService
    const token = this.authService.getToken();
    if (!token) {
      throw new Error('Token non trouvé - l’utilisateur n’est pas authentifié');
    }

    // Set up headers with the token
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    // Make a POST request to create the post, including the subjectId as a query parameter
    return this.http.post<Post>(
      `${this.apiUrl}?subjectId=${subjectId}`,
      postData,
      { headers }
    );
  }
}
