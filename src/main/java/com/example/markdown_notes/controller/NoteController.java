package com.example.markdown_notes.controller;

import com.example.markdown_notes.entity.Note;
import com.example.markdown_notes.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteService.createNote(note);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<java.util.List<Note>> getAllNotes() {
        java.util.List<Note> notes = noteService.getAllNotes();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        java.util.Optional<Note> noteOptional = noteService.getNoteById(id);

        if (noteOptional.isPresent()) {
            return new ResponseEntity<>(noteOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}/html", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> getNoteAsHtml(@PathVariable Long id) {
        return noteService.getNoteAsHtml(id)
                .map(html -> new ResponseEntity<>(html, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}