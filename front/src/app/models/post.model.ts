export interface Post {
  id?: number; // ID de l'article (facultatif pour un nouvel article)
  title: string; // Titre de l'article
  content: string; // Contenu de l'article
  authorId: number; // ID de l'auteur
  subjectId: number; // ID du thème ou sujet de l'article
  createdAt?: Date; // Date de création (facultatif)
}
