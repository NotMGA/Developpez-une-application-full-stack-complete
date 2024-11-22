import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss'],
})
export class FeedComponent implements OnInit {
  articles: any[] = [];

  errorMessage: string | null = null;

  isSortedDescending: boolean = true;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    const token = localStorage.getItem('token'); // Retrieve the user's authentication token
    if (!token) {
      this.errorMessage =
        'Vous devez être connecté pour voir le fil d’actualité';
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http
      .get('http://localhost:8080/api/posts/feed?userId=1', { headers })
      .subscribe(
        (response: any) => {
          this.articles = response;
          this.sortArticlesByDate(); // Automatically sort articles by date on load
        },
        (error) => {
          console.error('Erreur lors du chargement du fil d’actualité', error);
          this.errorMessage = 'Erreur lors du chargement du fil d’actualité';
        }
      );
  }

  /**
   * Navigates to the details page of a specific article.
   * @param postId The ID of the article to view details for.
   */
  viewDetails(postId: number): void {
    this.router.navigate(['/article', postId]);
  }

  /**
   * Navigates to the article creation page.
   */
  createArticle(): void {
    this.router.navigate(['/create']);
  }

  /**
   * Sorts the articles by their creation date.
   * Toggles between ascending and descending order on each call.
   */
  sortArticlesByDate(): void {
    // Toggle the sorting order
    this.isSortedDescending = !this.isSortedDescending;

    // Sort the articles based on the createdAt date
    this.articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();

      return this.isSortedDescending ? dateB - dateA : dateA - dateB;
    });
  }
}
