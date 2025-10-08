import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { NoteDetailComponent } from './note-detail/note-detail';

export const appRoutes: Routes = [
  { path: '', component: AppComponent },
  { path: 'note/:id', component: NoteDetailComponent },
  { path: '**', redirectTo: '' },
];


