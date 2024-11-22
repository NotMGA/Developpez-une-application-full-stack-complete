export interface Post {
  id?: number;
  title: string;
  content: string;
  authorId: number;
  subjectId: number;
  createdAt?: Date;
}
