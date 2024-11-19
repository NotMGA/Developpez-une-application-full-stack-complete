import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent {
  @Input() theme: any; // Le thème à afficher
  @Input() isProfilePage: boolean = false; // Détermine si la carte est affichée sur la page de profil
  @Output() subscribe = new EventEmitter<number>(); // Événement pour s'abonner
  @Output() unsubscribe = new EventEmitter<number>(); // Événement pour se désabonner

  // Méthode pour gérer l'action d'abonnement/désabonnement
  handleAction(): void {
    if (this.isProfilePage) {
      this.unsubscribe.emit(this.theme.id);
    } else {
      this.subscribe.emit(this.theme.id);
    }
  }
}
