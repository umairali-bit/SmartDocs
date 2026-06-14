package com.example.smartDocs.services;

import com.example.smartDocs.entities.UploadDocument;
import com.example.smartDocs.entities.UploadedDocumentChunk;
import com.example.smartDocs.repositories.UploadedDocumentChunkRepository;
import com.example.smartDocs.repositories.UploadedDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartDocsIngestionService {

    private final VectorStore vectorStore;
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final UploadedDocumentChunkRepository uploadedDocumentChunkRepository;

    public void ingestDocument(Resource resource,
                               String documentId) {

        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(resource);

        List<Document> pages = reader.read();

        pages.forEach(page -> {
            page.getMetadata().put(
                    "documentId",
                    documentId
            );

            page.getMetadata().put(
                    "fileName",
                    resource.getFilename()
            );
        });

        UploadDocument uploadedDocument = UploadDocument.builder()
                .documentId(documentId)
                .fileName(resource.getFilename())
                .pageCount(pages.size())
                .build();

        uploadedDocumentRepository.save(uploadedDocument);

        TokenTextSplitter splitter =
                TokenTextSplitter.builder()
                        .withChunkSize(200)
                        .withMinChunkSizeChars(50)
                        .build();

        List<Document> chunks =
                splitter.split(pages);

        vectorStore.add(chunks);

        for (int i = 0; i < chunks.size(); i++) {
            UploadedDocumentChunk entity = UploadedDocumentChunk.builder()
                    .documentId(documentId)
                    .chunkNumber(i + 1)
                    .content(chunks.get(i).getText())
                    .build();

            uploadedDocumentChunkRepository.save(entity);
        }
    }
}
