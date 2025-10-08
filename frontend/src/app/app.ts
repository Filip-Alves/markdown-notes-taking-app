import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AppComponent } from './app.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AppComponent],
  template: `<router-outlet></router-outlet>`,
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('my-app');
}
