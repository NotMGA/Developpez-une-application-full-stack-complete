import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  /**
   * Intercepts all outgoing HTTP requests and adds an `Authorization` header
   * if a valid token is present.
   *
   * @param req The outgoing HTTP request.
   * @param next The HTTP handler for processing the request.
   * @returns An observable of the HTTP event.
   */
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Retrieve the token from the AuthService
    const token = this.authService.getToken();

    // Check if a token exists
    if (token) {
      // Clone the original request and add the Authorization header
      const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      });

      // Forward the cloned request with the token
      return next.handle(clonedRequest);
    } else {
      // Forward the original request if no token is available
      return next.handle(req);
    }
  }
}
