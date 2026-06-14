package com.example.smartDocs.services;

import com.example.smartDocs.advisors.TokenUsageAdvisor;
import com.example.smartDocs.entities.UploadDocument;
import com.example.smartDocs.entities.UploadedDocumentChunk;
import com.example.smartDocs.repositories.UploadedDocumentChunkRepository;
import com.example.smartDocs.repositories.UploadedDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SmartDocsService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final UploadedDocumentChunkRepository uploadedDocumentChunkRepository;

    public String askDocument(String documentId,
                              String question,
                              String conversationId) {

        List<Document> docs =
                vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(question)
                                .filterExpression(
                                        "documentId == '" + documentId + "'"
                                )
                                .topK(3)
                                .similarityThreshold(0.0)
                                .build()
                );

        System.out.println("\n======================");
        System.out.println("QUESTION = " + question);
        System.out.println("DOCS FOUND = " + docs.size());

        for (int i = 0; i < docs.size(); i++) {
            System.out.println("\nCHUNK " + (i + 1));
            System.out.println(docs.get(i).getText());
        }
        System.out.println("======================");

        return chatClient.prompt()
                .system("""
                        You are SmartDocs AI.
                        
                        Answer ONLY using retrieved document context.
                        
                        If the answer exists in the context,
                        answer using that information.
                        
                        If the answer does not exist in the context,
                        respond exactly:
                        
                        "I could not find that information in the document."
                        """)
                .user(question)
                .advisors(

//                        new SafeGuardAdvisor(
//                                List.of(
//                                        "Hate Speech",
//                                        "Violence",
//                                        "Illegal Activities"
//                                )
//                        ),

                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build(),

                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .filterExpression(
                                                        "documentId == '" + documentId + "'"
                                                )
                                                .topK(3)
                                                .similarityThreshold(0.0)
                                                .build()
                                )
                                .build()
                )
                .call()
                .content();
    }


}
