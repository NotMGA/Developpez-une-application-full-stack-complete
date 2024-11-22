import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  /**
   * When set to `true`, the navigation menu will be hidden, leaving only the logo visible.
   */
  @Input() isAuthPage: boolean = false;

  /**
   * Controls the visibility of the navigation menu on mobile devices.
   * Toggles between open and closed states when the menu button is clicked.
   */
  isMenuOpen = false;
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
}
