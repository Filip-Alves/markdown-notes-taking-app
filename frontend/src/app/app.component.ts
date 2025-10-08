import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { marked } from 'marked';
import { NotesService } from './services/notes.service';
import { Note } from './models/note.model';

@Component({
  selector: 'app-notes-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  notes: Note[] = [];
  newNote = { title: '', content: '' };
  editingNote: Note = { id: 0, title: '', content: '' };
  uploadTitle = '';
  selectedFile: File | null = null;
  grammarErrors: string[] = [];

  constructor(private notesService: NotesService, private router: Router) {}

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  uploadFile() {
    if (!this.selectedFile) return;

    this.notesService.uploadMarkdown(this.uploadTitle, this.selectedFile).subscribe({
      next: (note) => {
        this.notes.push(note);
        this.uploadTitle = '';
        this.selectedFile = null;
      },
      error: (err) => console.error('Erreur lors de l’upload', err),
    });
  }
  ngOnInit() {
    this.loadNotes();
  }

  loadNotes() {
    this.notesService.getNotes().subscribe({
      next: (data) => (this.notes = data),
      error: (err) => console.error('Erreur lors du chargement des notes', err),
    });
  }

  createNote() {
    this.notesService.createNote(this.newNote).subscribe({
      next: (note) => {
        this.notes.push(note);
        this.newNote = { title: '', content: '' };
      },
      error: (err) => console.error('Erreur lors de la création', err),
    });
  }

  viewNote(id: number) {
    this.router.navigate(['/note', id]);
  }

  startEdit(note: Note) {
    this.editingNote = { ...note };
  }

  updateNote() {
    if (!this.editingNote.id) return;
    this.notesService.updateNote(this.editingNote).subscribe({
      next: (updatedNote) => {
        const index = this.notes.findIndex((n) => n.id === updatedNote.id);
        if (index !== -1) {
          this.notes[index] = updatedNote;
        }
        this.cancelEdit();
      },
      error: (err) => console.error('Erreur lors de la mise à jour', err),
    });
  }

  cancelEdit() {
    this.editingNote = { id: 0, title: '', content: '' };
  }

  deleteNote(id: number) {
    this.notesService.deleteNote(id).subscribe({
      next: () => (this.notes = this.notes.filter((n) => n.id !== id)),
      error: (err) => console.error('Erreur lors de la suppression', err),
    });
  }

  renderMarkdown(markdown: string): string {
    return marked.parse(markdown) as string;
  }

  checkGrammar() {
    console.log('Bouton "Vérifier la grammaire" cliqué');
    if (!this.newNote.content) {
      alert('Aucun contenu à vérifier.');
      return;
    }

    console.log('Envoi du texte pour vérification grammaticale :', this.newNote.content);

    // Keeping grammar check via fetch to avoid leaking HttpClient usage here
    fetch('http://localhost:8080/api/grammar/check', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: this.newNote.content }),
    })
      .then((r) => r.json())
      .then((response) => {
        this.grammarErrors = response.map((err: any) => err.message);
      })
      .catch((err) => console.error('Erreur lors de la vérification grammaticale', err));
  }
}
