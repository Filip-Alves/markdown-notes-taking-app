package com.example.markdown_notes.service;

import com.example.markdown_notes.dto.CreateNoteRequestDTO;
import com.example.markdown_notes.entity.Note;
import com.example.markdown_notes.exception.ResourceNotFoundException;
import com.example.markdown_notes.repository.NoteRepository;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    public Note createNote(CreateNoteRequestDTO request) {
        Note newNote = new Note(request.getTitle(), request.getContent());
        return noteRepository.save(newNote);
    }

    public java.util.List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'ID : " + id));
    }

    public Note updateNote(Long id, CreateNoteRequestDTO request) {

        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Impossible de mettre à jour. Note non trouvée avec l'ID : \" + id" + id));

        existingNote.setTitle(request.getTitle());
        existingNote.setContent(request.getContent());

        return noteRepository.save(existingNote);
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer. Note non trouvée avec l'ID : " + id);
        }
        noteRepository.deleteById(id);
    }

    public String getNoteAsHtml(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'ID : " + id));

        Node document = markdownParser.parse(note.getContent());
        return htmlRenderer.render(document);
    }

}
