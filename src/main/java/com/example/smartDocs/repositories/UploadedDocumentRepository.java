package com.example.smartDocs.repositories;


import com.example.smartDocs.entities.UploadDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadDocument, String> {



}
