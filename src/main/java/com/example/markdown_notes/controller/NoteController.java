package com.example.markdown_notes.controller;

import com.example.markdown_notes.dto.CreateNoteRequestDTO;
import com.example.markdown_notes.entity.Note;
import com.example.markdown_notes.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody CreateNoteRequestDTO request) {
        Note createdNote = noteService.createNote(request);
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

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        return noteService.updateNote(id, noteDetails)
                .map(updatedNote -> new ResponseEntity<>(updatedNote, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (noteService.deleteNote(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    // Dans NoteController.java

    @PostMapping("/upload")
    public ResponseEntity<Note> uploadNote(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Le fichier ne peut pas être vide.");
            }
            String content = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);

            CreateNoteRequestDTO request = new CreateNoteRequestDTO(title, content);
            Note createdNote = noteService.createNote(request);

            return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) { // fichier vide
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}