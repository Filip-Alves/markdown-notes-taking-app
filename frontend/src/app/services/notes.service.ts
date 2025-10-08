import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Note } from '../models/note.model';

@Injectable({ providedIn: 'root' })
export class NotesService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api/notes';

  getNotes(): Observable<Note[]> {
    return this.http.get<Note[]>(this.baseUrl);
  }

  getNote(id: number): Observable<Note> {
    return this.http.get<Note>(`${this.baseUrl}/${id}`);
  }

  getNoteHtml(id: number): Observable<string> {
    return this.http.get(`${this.baseUrl}/${id}/html`, { responseType: 'text' });
  }

  createNote(payload: Pick<Note, 'title' | 'content'>): Observable<Note> {
    return this.http.post<Note>(this.baseUrl, payload);
  }

  updateNote(note: Note): Observable<Note> {
    return this.http.put<Note>(`${this.baseUrl}/${note.id}`, note);
  }

  deleteNote(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  uploadMarkdown(title: string, file: File): Observable<Note> {
    const formData = new FormData();
    formData.append('title', title);
    formData.append('file', file);
    return this.http.post<Note>(`${this.baseUrl}/upload`, formData);
  }
}


