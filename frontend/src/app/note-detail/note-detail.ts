import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotesService } from '../services/notes.service';
import { Note } from '../models/note.model';

@Component({
  selector: 'app-note-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4">
      <button class="btn btn-secondary mb-3" (click)="goBack()">Retour</button>
      <div class="card" *ngIf="note">
        <div class="card-body">
          <h5 class="card-title">{{ note.title }}</h5>
          <div class="card-text" [innerHTML]="htmlContent"></div>
        </div>
      </div>
    </div>
  `,
})
export class NoteDetailComponent implements OnInit {
  note: Note | null = null;
  htmlContent = '';

  constructor(private route: ActivatedRoute, private notesService: NotesService, private router: Router) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.getNoteById(id);
  }

  getNoteById(id: number) {
    this.notesService.getNote(id).subscribe({
      next: (data) => {
        this.note = data;
        this.getHtmlContent(id);
      },
      error: (err) => console.error('Erreur lors de la récupération de la note', err),
    });
  }

  getHtmlContent(id: number) {
    this.notesService.getNoteHtml(id).subscribe({
      next: (html) => {
        this.htmlContent = html;
      },
      error: (err) => console.error('Erreur lors de la récupération du HTML', err),
    });
  }

  goBack() {
    this.router.navigate(['/']);
  }
}
