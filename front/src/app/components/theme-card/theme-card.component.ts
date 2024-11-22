import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-theme-card',
  templateUrl: './theme-card.component.html',
  styleUrls: ['./theme-card.component.scss'],
})
export class ThemeCardComponent {
  /**
   * The theme object to be displayed in the card.
   * Contains information about the theme, such as its ID and other details.
   */
  @Input() theme: any;

  /**
   * Indicates whether the card is being displayed on the profile page.
   * On the profile page, the card will show an "Unsubscribe" button instead of "Subscribe".
   */
  @Input() isProfilePage: boolean = false;

  /**
   * Event emitted when the user wants to subscribe to the theme.
   * Sends the theme's ID as a payload.
   */
  @Output() subscribe = new EventEmitter<number>();

  /**
   * Event emitted when the user wants to unsubscribe from the theme.
   * Sends the theme's ID as a payload.
   */
  @Output() unsubscribe = new EventEmitter<number>();

  /**
   * Handles the action for subscribing or unsubscribing to a theme.
   * - If the card is displayed on the profile page (`isProfilePage = true`), it emits an unsubscribe event.
   * - Otherwise, it emits a subscribe event.
   */
  handleAction(): void {
    if (this.isProfilePage) {
      this.unsubscribe.emit(this.theme.id);
    } else {
      this.subscribe.emit(this.theme.id);
    }
  }
}
