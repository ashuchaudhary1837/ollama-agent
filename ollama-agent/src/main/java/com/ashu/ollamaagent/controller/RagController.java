
package com.ashu.ollamaagent.controller;

import com.ashu.ollamaagent.service.RagService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadDocument(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Please upload a non-empty file")
            );
        }

        String filename = file.getOriginalFilename();

        if (filename == null ||
                !filename.toLowerCase().endsWith(".txt")) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Only .txt files are supported")
            );
        }

        String text = new String(
                file.getBytes(),
                StandardCharsets.UTF_8
        );

        ragService.ingestText(text);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Document ingested successfully",
                        "filename", filename
                )
        );
    }
}
