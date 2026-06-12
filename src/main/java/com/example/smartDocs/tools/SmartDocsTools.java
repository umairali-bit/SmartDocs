package com.example.smartDocs.tools;


import com.example.smartDocs.entities.UploadedDocumentChunk;
import com.example.smartDocs.repositories.UploadedDocumentChunkRepository;
import com.example.smartDocs.repositories.UploadedDocumentRepository;
import com.example.smartDocs.service.SmartDocsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SmartDocsTools {

    private final SmartDocsService smartDocsService;
    private final UploadedDocumentChunkRepository uploadedDocumentChunkRepository;
    private final UploadedDocumentRepository uploadedDocumentRepository;

    @Tool(description = "Retrieve the complete document text")
    public String getEntireDocument(String documentId) {

        List<UploadedDocumentChunk> chunks =
                uploadedDocumentChunkRepository.findByDocumentIdOrderByChunkNumberAscending(documentId);
        return chunks.stream()
                .map(i -> i.getContent())
                .collect(Collectors.joining("\n"));

    }

    @Tool(description = "Get the total number of pages in a document")
    public Integer getTotalNumberOfPages(String documentId) {
        return uploadedDocumentRepository
                .findById(documentId)
                .map(document -> document.getPageCount())
                .orElse(0);
    }



}
