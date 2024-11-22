import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { ArticleService } from '../../services/api.article.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss'],
})
export class ArticleDetailsComponent implements OnInit {
  article: any;

  errorMessage: string = '';

  newComment: string = '';

  comments: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private articleService: ArticleService,
    private location: Location
  ) {}

  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.loadArticleDetails(parseInt(postId, 10));
    }
  }

  /**
   * Loads the details of an article using the provided post ID.
   * @param postId The ID of the article to load.
   */
  loadArticleDetails(postId: number): void {
    this.apiService.getPostById(postId).subscribe(
      (response) => {
        this.article = response;
        console.log('Détails de l’article chargés avec succès:', this.article);
        this.loadComments(postId);
      },
      (error) => {
        console.error(
          'Erreur lors du chargement des détails de l’article',
          error
        );
        if (error.status === 403) {
          this.errorMessage =
            'Vous n’êtes pas autorisé à consulter cet article. Veuillez vous connecter.';
        } else {
          this.errorMessage = 'Impossible de charger les détails de l’article.';
        }
      }
    );
  }

  /**
   * Posts a new comment for the current article.
   */
  postComment(): void {
    if (this.newComment.trim() !== '') {
      const commentDTO = {
        postId: this.article.id, // Retrieve the article ID
        content: this.newComment,
      };

      this.articleService.postComment(commentDTO).subscribe(
        (response: any) => {
          console.log('Commentaire envoyé avec succès', response);

          // Ensure comments are initialized before adding the new comment
          if (!this.comments) {
            this.comments = [];
          }

          this.comments.push(response); // Add the new comment to the list
          this.newComment = ''; // Reset the input field
        },
        (error: any) => {
          console.error("Erreur lors de l'envoi du commentaire", error);
          this.errorMessage = "Impossible d'envoyer le commentaire.";
        }
      );
    }
  }

  /**
   * Loads the comments associated with the current article.
   * @param postId The ID of the article whose comments are to be loaded.
   */
  loadComments(postId: number): void {
    this.apiService.getCommentsByPost(postId).subscribe(
      (response) => {
        this.comments = response; // Ensure `comments` is a valid array
        console.log('Commentaires chargés avec succès:', this.comments);
      },
      (error) => {
        console.error('Erreur lors du chargement des commentaires', error);
        this.errorMessage = 'Impossible de charger les commentaires.';
      }
    );
  }

  /**
   * Navigates back to the previous page.
   */
  goBack(): void {
    this.location.back();
  }
}
