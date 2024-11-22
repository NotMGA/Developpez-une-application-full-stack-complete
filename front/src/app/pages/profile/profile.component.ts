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

  /**
   * Loads the user's profile data from the backend.
   */
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

  /**
   * Loads the list of subjects the user is subscribed to.
   */
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

  /**
   * Saves updates to the user's profile by sending the updated data to the backend.
   */
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

  /**
   * Unsubscribes the user from a specific theme.
   * @param themeId The ID of the theme to unsubscribe from.
   */
  unsubscribeFromTheme(themeId: number): void {
    this.subjectService.unsubscribeFromSubject(themeId).subscribe(
      () => {
        alert('Désabonné du thème avec succès.');
        this.loadUserSubscriptions(); // Reload subscriptions to reflect changes
      },
      (error) => {
        console.error('Erreur lors du désabonnement', error);
        this.errorMessage = 'Impossible de se désabonner du thème.';
      }
    );
  }

  /**
   * Logs out the user by clearing the authentication token and redirecting to the login page.
   */
  logout(): void {
    this.authService.clearToken();
    window.location.href = '/login'; // Redirect to login page
  }
}
