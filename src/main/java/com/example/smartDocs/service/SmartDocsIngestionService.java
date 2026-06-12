package com.example.smartDocs.service;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartDocsIngestionService {

    private final PgVectorStore vectorStore;

    public void ingestDocument(Resource resource, String documentId){
        PagePdfDocumentReader  reader = new PagePdfDocumentReader(resource);
        List<Document> pages = reader.read();
        pages.forEach(document -> {
            document.getMetadata()
                    .put("documentId", documentId);
        });

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();

        List<Document> chunks = splitter.split(pages);
        vectorStore.add(chunks);

    }
}
