package com.example.smartDocs.services;


import com.example.smartDocs.entities.UploadDocument;
import com.example.smartDocs.entities.UploadedDocumentChunk;
import com.example.smartDocs.repositories.UploadedDocumentChunkRepository;
import com.example.smartDocs.repositories.UploadedDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartDocsIngestionService {

    private final PgVectorStore vectorStore;
    private final UploadedDocumentChunkRepository uploadedDocumentChunkRepository;
    private final UploadedDocumentRepository uploadedDocumentRepository;

    public void ingestDocument(Resource resource, String documentId){

//       Read PDF pages
        PagePdfDocumentReader  reader = new PagePdfDocumentReader(resource);
        List<Document> pages = reader.read();

//        Add meta data for filtering
        pages.forEach(document -> {
            document.getMetadata()
                    .put("documentId", documentId);
        });

//        Save document metaData

        UploadDocument uploadedDocument =
                UploadDocument.builder()
                        .documentId(documentId)
                        .fileName(resource.getFilename())
                        .pageCount(pages.size())
                        .build();

        uploadedDocumentRepository.save(uploadedDocument);

//      Split the document into chunks

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();

        List<Document> chunks = splitter.split(pages);

//        Store embeddings in PGVector
        vectorStore.add(chunks);

//        Store original chunks for whole-document operations

        for (int i = 0; i < chunks.size(); i++) {

            Document chunk = chunks.get(i);

            UploadedDocumentChunk entity =
                    new UploadedDocumentChunk();

            entity.setDocumentId(documentId);
            entity.setChunkNumber(i + 1);
            entity.setContent(chunk.getText());

            uploadedDocumentChunkRepository.save(entity);
        }

    }
}
