import { Component, OnInit } from '@angular/core';
import { SubjectService } from 'src/app/services/api.subject.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss'],
})
export class ThemesComponent implements OnInit {
  subjects: any[] = [];
  errorMessage: string = '';
  Message: string = '';

  constructor(private subjectService: SubjectService) {}

  ngOnInit(): void {
    this.loadSubjects();
  }

  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe(
      (response) => {
        this.subjects = response;
      },
      (error) => {
        console.error('Erreur lors du chargement des thèmes', error);
        this.errorMessage = 'Impossible de charger les thèmes.';
      }
    );
  }

  subscribeToSubject(subjectId: number): void {
    this.subjectService.subscribeToSubject(subjectId).subscribe(
      (response) => {
        console.log(response.message);
        this.errorMessage = 'Vous êtes maintenant abonner.';
      },
      (error) => {
        console.error('Erreur lors de l’abonnement au thème', error);
        this.errorMessage = error.error.message || error.message;
        if (
          error.status === 409 &&
          error.error?.error === 'User is already subscribed to this subject'
        ) {
          this.errorMessage = 'Vous êtes déjà abonné à ce thème.';
        } else {
          this.errorMessage = error.error.message || error.message;
        }
      }
    );
  }
}
