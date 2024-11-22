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
    // Retrieve the token from local storage
    const token = localStorage.getItem('token');

    if (token) {
      // If the user is authenticated and trying to access the home route ('/'), redirect to "feed"
      if (state.url === '/') {
        this.router.navigate(['/feed']);
        return false;
      }
      return true;
    } else {
      // If no token is found, redirect the user to the "login" page
      this.router.navigate(['/login']);
      return false;
    }
  }
}
