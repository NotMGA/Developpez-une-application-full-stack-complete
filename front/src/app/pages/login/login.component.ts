import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginResponse } from './login-response.interface';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage = '';

  constructor(
    private apiService: ApiService,
    private router: Router,
    private fb: FormBuilder
  ) {
    // Initialisation du formulaire de connexion
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  login() {
    if (this.loginForm.valid) {
      // Récupération des données du formulaire
      const loginData = this.loginForm.value;

      this.apiService.login(loginData).subscribe(
        (response: LoginResponse) => {
          const token = response.token.replace('Bearer ', ''); // Enlever le préfixe "Bearer "
          localStorage.setItem('token', token); // Stocker uniquement le JWT dans le localStorage
          this.router.navigate(['/feed']); // Rediriger l'utilisateur vers la page du feed
        },
        (error) => {
          console.error('Erreur lors de la connexion', error);
          this.errorMessage =
            'Erreur lors de la connexion, vérifiez vos identifiants.';
        }
      );
    } else {
      this.errorMessage = 'Veuillez remplir correctement le formulaire.';
    }
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
  goBack() {
    this.router.navigate(['/home']);
  }
}
