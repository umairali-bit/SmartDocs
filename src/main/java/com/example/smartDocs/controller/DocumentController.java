package com.example.smartDocs.controller;


import com.example.smartDocs.dtos.ChatRequestDTO;
import com.example.smartDocs.dtos.ChatResponseDTO;
import com.example.smartDocs.dtos.UploadResponseDTO;
import com.example.smartDocs.services.SmartDocsIngestionService;
import com.example.smartDocs.services.SmartDocsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final SmartDocsIngestionService ingestionService;
    private final SmartDocsService  smartDocsService;


    @PostMapping("/upload")
    public UploadResponseDTO uploadDocument(
            @RequestParam("file") MultipartFile file)
            throws IOException {
        String documentId = UUID.randomUUID().toString();

        ingestionService.ingestDocument(
                file.getResource(),
                documentId
        );

        return new UploadResponseDTO(
                documentId,
                file.getOriginalFilename()
        );
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> askQuestion(@RequestBody ChatRequestDTO request) {

        String answer = smartDocsService.askDocument(
                request.documentId(),
                request.question(),
                request.conversationId()
        );

        return ResponseEntity.ok(new ChatResponseDTO(answer));
    }

}
