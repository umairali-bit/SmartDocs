package com.example.smartDocs.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDocument {


    @Id
    private String documentId;

    private String fileName;

    private Integer pageCount;

    @CreationTimestamp
    private LocalDateTime uploadDate;
}
