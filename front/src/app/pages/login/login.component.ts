import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginResponse } from './login-response.interface';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  errorMessage = '';

  fields = [
    {
      name: 'email',
      label: 'Email',
      type: 'email',
      validators: [Validators.required, Validators.email],
    },
    {
      name: 'password',
      label: 'Mot de passe',
      type: 'password',
      validators: [Validators.required, Validators.minLength(6)],
    },
  ];

  constructor(
    private apiService: ApiService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group(
      this.fields.reduce((acc, field) => {
        acc[field.name] = ['', field.validators];
        return acc;
      }, {} as any)
    );
  }

  /**
   * Submits the login form data to the API for authentication.
   * On success, stores the JWT token and navigates to the "feed" page.
   * @param formData Object containing the email and password entered by the user.
   */
  onLoginSubmit(formData: any): void {
    this.apiService.login(formData).subscribe(
      (response: LoginResponse) => {
        const token = response.token.replace('Bearer ', ''); // Remove "Bearer " prefix
        localStorage.setItem('token', token); // Store the JWT token in localStorage
        this.router.navigate(['/feed']); // Redirect to the feed page
      },
      (error) => {
        console.error('Erreur lors de la connexion', error);
        this.errorMessage =
          'Erreur lors de la connexion, vérifiez vos identifiants.';
      }
    );
  }

  /**
   * Navigates back to the home page.
   */
  goBack(): void {
    this.router.navigate(['/home']);
  }
}
