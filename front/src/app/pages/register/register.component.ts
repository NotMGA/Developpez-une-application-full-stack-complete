import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  errorMessage: string = '';

  fields = [
    {
      name: 'email',
      label: 'Email',
      type: 'email',
      validators: [Validators.required, Validators.email],
    },
    {
      name: 'username',
      label: "Nom d'utilisateur",
      type: 'text',
      validators: [Validators.required],
    },
    {
      name: 'password',
      label: 'Mot de passe',
      type: 'password',
      validators: [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(
          '(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].{7,}'
        ),
      ],
    },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group(
      this.fields.reduce((acc, field) => {
        acc[field.name] = ['', field.validators];
        return acc;
      }, {} as any)
    );
  }

  /**
   * Handles form submission by sending the form data to the backend API.
   * On success, navigates the user to the login page.
   * @param formData The data submitted from the form.
   */
  onRegisterSubmit(formData: any): void {
    this.apiService.register(formData).subscribe(
      (response) => {
        this.router.navigate(['/login']); // Redirect to login page on success
      },
      (error) => {
        this.errorMessage = "Erreur lors de l'inscription. Veuillez réessayer.";
        console.error("Erreur lors de l'inscription", error);
      }
    );
  }

  /**
   * Navigates back to the previous page.
   */
  goBack(): void {
    this.location.back();
  }
}
