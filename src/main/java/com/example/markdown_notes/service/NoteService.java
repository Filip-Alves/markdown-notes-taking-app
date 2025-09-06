package com.example.markdown_notes.service;

import com.example.markdown_notes.entity.Note;
import com.example.markdown_notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository noteRepository;


    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note noteToCreate) {
        return noteRepository.save(noteToCreate);
    }

    public java.util.List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public java.util.Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
