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
  comments: any[] = []; // Liste des commentaires

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

  loadArticleDetails(postId: number) {
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
  postComment(): void {
    if (this.newComment.trim() !== '') {
      const commentDTO = {
        postId: this.article.id, // Récupérer l'id de l'article
        content: this.newComment,
      };

      // Envoyer le commentaire via le service
      this.articleService.postComment(commentDTO).subscribe(
        (response: any) => {
          console.log('Commentaire envoyé avec succès', response);

          // Vérifiez que `comments` est initialisé
          if (!this.comments) {
            this.comments = [];
          }

          // Ajouter le nouveau commentaire à la liste des commentaires
          this.comments.push(response);

          // Réinitialiser le champ de saisie après envoi
          this.newComment = '';
        },
        (error: any) => {
          console.error("Erreur lors de l'envoi du commentaire", error);
          this.errorMessage = "Impossible d'envoyer le commentaire.";
        }
      );
    }
  }

  loadComments(postId: number) {
    this.apiService.getCommentsByPost(postId).subscribe(
      (response) => {
        this.comments = response; // Assurez-vous que `comments` est un tableau
        console.log('Commentaires chargés avec succès:', this.comments);
      },
      (error) => {
        console.error('Erreur lors du chargement des commentaires', error);
        this.errorMessage = 'Impossible de charger les commentaires.';
      }
    );
  }

  goBack() {
    this.location.back(); // Retour à la page précédente
  }
}
