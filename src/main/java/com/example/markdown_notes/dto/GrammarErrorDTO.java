package com.example.markdown_notes.dto;

import lombok.Data;
import java.util.List;

@Data
public class GrammarErrorDTO {
    private String message;
    private List<String> suggestedReplacements;
    private int offset;
    private int length;
}