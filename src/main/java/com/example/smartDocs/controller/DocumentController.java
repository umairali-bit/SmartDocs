package com.example.smartDocs.controller;


import com.example.smartDocs.dtos.UploadResponseDTO;
import com.example.smartDocs.services.SmartDocsIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final SmartDocsIngestionService ingestionService;

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
}
