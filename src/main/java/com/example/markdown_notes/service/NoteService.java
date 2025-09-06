package com.example.markdown_notes.service;

import com.example.markdown_notes.entity.Note;
import com.example.markdown_notes.repository.NoteRepository;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;


    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;

        // Configuration de flexmark
        MutableDataSet options = new MutableDataSet();
        this.markdownParser = Parser.builder(options).build();
        this.htmlRenderer = HtmlRenderer.builder(options).build();
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

    public java.util.Optional<Note> updateNote(Long id, Note noteDetails) {
        java.util.Optional<Note> optionalNote = noteRepository.findById(id);

        if (optionalNote.isEmpty()) {
            return java.util.Optional.empty();
        }

        Note existingNote = optionalNote.get();
        existingNote.setTitle(noteDetails.getTitle());
        existingNote.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(existingNote);
        return java.util.Optional.of(updatedNote);
    }

    public boolean deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public java.util.Optional<String> getNoteAsHtml(Long id) {
        java.util.Optional<Note> noteOptional = noteRepository.findById(id);

        if (noteOptional.isEmpty()) {
            return java.util.Optional.empty();
        }

        Note note = noteOptional.get();
        Node document = markdownParser.parse(note.getContent());
        String html = htmlRenderer.render(document);

        return java.util.Optional.of(html);
    }
}
