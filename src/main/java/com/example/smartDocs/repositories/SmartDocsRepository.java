package com.example.smartDocs.repositories;


import com.example.smartDocs.entities.UploadedDocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmartDocsRepository extends JpaRepository<UploadedDocumentChunk, Long> {

    List<UploadedDocumentChunk> findByDocumentIdOrderByChunkNumberAscending(String documentId);
}
