import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { AuthGuard } from './guards/auth.guard';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleDetailsComponent } from './pages/article-details/article-details.component';
import { CreateArticleComponent } from './pages/create-article/create-article.component';
import { ThemesComponent } from './pages/themes/themes.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'feed', component: FeedComponent, canActivate: [AuthGuard] },
  {
    path: 'create',
    component: CreateArticleComponent,
    canActivate: [AuthGuard],
  },
  { path: 'article/:id', component: ArticleDetailsComponent },
  { path: 'themes', component: ThemesComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] }, // Protéger le profil avec AuthGuard
  {
    path: 'articles/:id',
    component: ArticleDetailsComponent,
    canActivate: [AuthGuard],
  },
  { path: '**', redirectTo: '' }, // Rediriger toutes les routes non trouvées vers 'login'
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
