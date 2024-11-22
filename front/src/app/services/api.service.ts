import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../pages/login/login-response.interface';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private authService: AuthService) {}

  /**
   * Creates HTTP headers with the Authorization token if the user is authenticated.
   * @returns An HttpHeaders object with the Authorization header set.
   */
  private createAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  /**
   * Registers a new user.
   * @param user The user data (e.g., email, username, password).
   * @returns An Observable of the server response.
   */
  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/register`, user);
  }

  /**
   * Logs in a user by sending their credentials to the API.
   * @param credentials An object containing the user's email and password.
   * @returns An Observable of the `LoginResponse` containing the authentication token.
   */
  login(credentials: {
    email: string;
    password: string;
  }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/users/login`,
      credentials
    );
  }

  /**
   * Retrieves the profile of a specific user.
   * @param userId The ID of the user whose profile is being retrieved.
   * @returns An Observable of the user's profile data.
   */
  getProfile(userId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get(`${this.apiUrl}/users/${userId}/profile`, { headers });
  }

  /**
   * Fetches the feed of posts for a specific user.
   * @param userId The ID of the user whose feed is being fetched.
   * @returns An Observable of the feed data.
   */
  getFeed(userId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get(`${this.apiUrl}/posts/feed?userId=${userId}`, {
      headers,
    });
  }

  /**
   * Fetches a specific post by its ID.
   * @param postId The ID of the post to retrieve.
   * @returns An Observable of the post data.
   */
  getPostById(postId: number): Observable<any> {
    const headers = this.createAuthHeaders();
    return this.http.get<any>(`${this.apiUrl}/posts/${postId}`, { headers });
  }

  /**
   * Retrieves the comments for a specific post.
   * @param postId The ID of the post whose comments are being fetched.
   * @returns An Observable of the comments data.
   */
  getCommentsByPost(postId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/comments/post/${postId}`);
  }
}
