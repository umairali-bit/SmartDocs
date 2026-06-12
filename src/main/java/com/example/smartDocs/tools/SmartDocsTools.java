package com.example.smartDocs.tools;


import com.example.smartDocs.entities.UploadedDocumentChunk;
import com.example.smartDocs.repositories.SmartDocsRepository;
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
    private final SmartDocsRepository smartDocsRepository;

    @Tool(description = "Retrieve the complete document text")
    public String getEntireDocument(String documentId) {

        List<UploadedDocumentChunk> chunks =
                smartDocsRepository.findByDocumentIdOrderByChunkNumberAscending(documentId);
        return chunks.stream()
                .map(i -> i.getContent())
                .collect(Collectors.joining("\n"));

    }



}
