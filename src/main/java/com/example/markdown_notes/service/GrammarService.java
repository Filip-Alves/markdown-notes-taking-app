package com.example.markdown_notes.service;

import com.example.markdown_notes.dto.GrammarCheckRequestDTO;
import com.example.markdown_notes.dto.GrammarErrorDTO;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrammarService {

    private final JLanguageTool langTool;

    public GrammarService() {
        // LA NOUVELLE FAÇON MODERNE D'INITIALISER
        // On récupère la langue "française" via la classe utilitaire Languages.
        this.langTool = new JLanguageTool(Languages.getLanguageForShortCode("fr"));
    }

    /**
     * Vérifie la grammaire du texte fourni.
     * @param request DTO contenant le texte à vérifier.
     * @return Une liste de DTOs représentant les erreurs trouvées.
     */
    public List<GrammarErrorDTO> checkGrammar(GrammarCheckRequestDTO request) {
        try {
            List<RuleMatch> matches = langTool.check(request.getText());
            return matches.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // Gérer l'exception de manière appropriée, par exemple en loggant l'erreur.
            throw new RuntimeException("Erreur lors de la vérification de la grammaire", e);
        }
    }

    /**
     * Méthode utilitaire pour convertir un objet RuleMatch de LanguageTool
     * en notre propre GrammarErrorDTO.
     */
    private GrammarErrorDTO convertToDto(RuleMatch match) {
        GrammarErrorDTO dto = new GrammarErrorDTO();
        dto.setMessage(match.getMessage());
        dto.setSuggestedReplacements(match.getSuggestedReplacements());
        dto.setOffset(match.getFromPos());
        dto.setLength(match.getToPos() - match.getFromPos());
        return dto;
    }
}