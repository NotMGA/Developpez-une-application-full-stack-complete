import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/api.user.service';
import { SubjectService } from 'src/app/services/api.subject.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user: any = {};
  subscriptions: any[] = [];
  errorMessage: string = '';

  constructor(
    private userService: UserService,
    private subjectService: SubjectService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadUserSubscriptions();
  }

  loadProfile(): void {
    this.userService.getUserProfile().subscribe(
      (response: any) => {
        this.user = response;
      },
      (error: any) => {
        console.error('Erreur lors du chargement du profil', error);
        this.errorMessage = 'Impossible de charger le profil.';
      }
    );
  }

  // Charger les abonnements de l'utilisateur
  loadUserSubscriptions(): void {
    this.subjectService.getUserSubscriptions().subscribe(
      (response: any) => {
        this.subscriptions = response;
      },
      (error: any) => {
        console.error('Erreur lors du chargement des abonnements', error);
        this.errorMessage = 'Impossible de charger les abonnements.';
      }
    );
  }

  // Sauvegarder les modifications de profil utilisateur
  onSaveProfile(): void {
    this.userService.updateUserProfile(this.user).subscribe(
      () => {
        alert('Profil mis à jour avec succès.');
      },
      (error) => {
        console.error('Erreur lors de la mise à jour du profil', error);
        this.errorMessage = 'Impossible de mettre à jour le profil.';
      }
    );
  }

  // Se désabonner d'un thème
  unsubscribeFromTheme(themeId: number): void {
    this.subjectService.unsubscribeFromSubject(themeId).subscribe(
      () => {
        alert('Désabonné du thème avec succès.');
        this.loadUserSubscriptions(); // Recharger la liste des abonnements
      },
      (error) => {
        console.error('Erreur lors du désabonnement', error);
        this.errorMessage = 'Impossible de se désabonner du thème.';
      }
    );
  }

  // Se déconnecter
  logout(): void {
    this.authService.clearToken();
    window.location.href = '/login'; // Redirection vers la page de connexion après déconnexion
  }
}
