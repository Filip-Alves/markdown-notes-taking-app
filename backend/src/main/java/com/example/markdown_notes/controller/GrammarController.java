package com.example.markdown_notes.controller;

import com.example.markdown_notes.dto.GrammarCheckRequestDTO;
import com.example.markdown_notes.dto.GrammarErrorDTO;
import com.example.markdown_notes.service.GrammarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grammar")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @PostMapping("/check")
    public ResponseEntity<List<GrammarErrorDTO>> checkGrammar(@RequestBody GrammarCheckRequestDTO request) {
        List<GrammarErrorDTO> errors = grammarService.checkGrammar(request);
        return ResponseEntity.ok(errors);
    }
}