package com.example.smartDocs.service;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmartDocsService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    public String ragImplementation(String documentId,String question, String conversationId) {
        return chatClient.prompt()
                .system("""
                        You are SmartDocs AI.

                        Answer questions ONLY from the uploaded document.

                        If the answer cannot be found in the document,
                        say:
                        "I could not find that information in the document."

                        Do not make up information.
                        """)
                .user(question)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(conversationId)
                                .build(),

                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .filterExpression(
                                                        "documentId == '" + documentId + "'"
                                                )
                                                .topK(5)
                                                .similarityThreshold(0.7)
                                                .build()
                                )
                                .build()
                )
                .call()
                .content();
    }

}
