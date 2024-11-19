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
    const token = localStorage.getItem('token'); // Assurez-vous que le token est stocké dans le localStorage lors de la connexion
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
          this.sortArticlesByDate();
        },
        (error) => {
          console.error('Erreur lors du chargement du fil d’actualité', error);
          this.errorMessage = 'Erreur lors du chargement du fil d’actualité';
        }
      );
  }

  viewDetails(postId: number): void {
    this.router.navigate(['/article', postId]);
  }
  createArticle(): void {
    // Navigation vers la page de création d'article
    this.router.navigate(['/create']);
  }
  sortArticlesByDate(): void {
    // Inverse la valeur de `isSortedDescending` à chaque appel pour alterner l'ordre du tri
    this.isSortedDescending = !this.isSortedDescending;

    this.articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();

      return this.isSortedDescending ? dateB - dateA : dateA - dateB;
    });
  }
}
