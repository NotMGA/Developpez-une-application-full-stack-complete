import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SubjectService } from 'src/app/services/api.subject.service';
import { PostService } from 'src/app/services/api.post.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss'],
})
export class CreateArticleComponent implements OnInit {
  articleForm: FormGroup;

  subjects: any[] = [];

  constructor(
    private fb: FormBuilder,
    private subjectService: SubjectService,
    private postService: PostService,
    private router: Router,
    private location: Location
  ) {
    this.articleForm = this.fb.group({
      subjectId: ['', Validators.required],
      title: ['', Validators.required],
      content: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadSubjects();
  }

  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe(
      (subjects) => {
        this.subjects = subjects;
      },
      (error) => {
        console.error('Erreur lors du chargement des sujets', error);
      }
    );
  }

  onSubmit(): void {
    if (this.articleForm.valid) {
      // Extract form data
      const postData = {
        title: this.articleForm.value.title,
        content: this.articleForm.value.content,
      };

      // Retrieve the selected subject ID
      const subjectId = this.articleForm.value.subjectId;

      // Call the service to create the post
      this.postService.createPost(postData, subjectId).subscribe(
        (response) => {
          console.log('Article créé avec succès', response);
        },
        (error) => {
          console.error('Erreur lors de la création de l’article', error);
        }
      );
      this.location.back(); // Navigate back after submission
    } else {
      console.log('Formulaire invalide');
    }
  }

  /**
   * Navigates back to the previous page.
   */
  goBack(): void {
    this.location.back();
  }
}
