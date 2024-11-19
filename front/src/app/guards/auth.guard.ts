import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const token = localStorage.getItem('token');

    if (token) {
      // Vérifiez si l'utilisateur essaie d'accéder à la page d'accueil
      if (state.url === '/') {
        // Redirigez vers "feed" s'il est authentifié
        this.router.navigate(['/feed']);
        return false;
      }
      return true; // Autorisez l'accès aux autres routes
    } else {
      // Redirigez vers "login" s'il n'est pas authentifié
      this.router.navigate(['/login']);
      return false;
    }
  }
}
