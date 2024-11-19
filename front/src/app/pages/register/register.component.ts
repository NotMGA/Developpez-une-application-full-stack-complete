import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private location: Location
  ) {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(
            '(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].{7,}'
          ),
        ],
      ],
    });
  }

  register() {
    if (this.registerForm.valid) {
      this.apiService.register(this.registerForm.value).subscribe(
        (response) => {
          // Inscription réussie, redirection vers la page de connexion ou profil
          this.router.navigate(['/login']);
        },
        (error) => {
          this.errorMessage =
            "Erreur lors de l'inscription. Veuillez réessayer.";
          console.error("Erreur lors de l'inscription", error);
        }
      );
    }
  }

  goBack() {
    this.location.back(); // Retour à la page précédente
  }
}
