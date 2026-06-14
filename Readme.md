# SmartDocs AI

SmartDocs AI is an intelligent document question-answering platform built with **Spring Boot**, **Spring AI**, **Ollama**, and **PGVector**. The application allows users to upload documents, generate vector embeddings, store them in a vector database, and interact with their documents through a Retrieval-Augmented Generation (RAG) pipeline.

## Overview

SmartDocs AI transforms uploaded documents into searchable knowledge bases. Instead of manually reading lengthy PDFs, users can ask natural language questions and receive context-aware responses generated from the document content.

The application combines:

* Document Processing
* Embedding Generation
* Vector Search
* Retrieval-Augmented Generation (RAG)
* Conversational Memory
* Large Language Models (LLMs)

---

# Architecture

```text
                    +------------------+
                    |      User        |
                    +--------+---------+
                             |
                             v
                  +----------------------+
                  |   Spring Boot API    |
                  +----------+-----------+
                             |
        +--------------------+--------------------+
        |                                         |
        v                                         v
+-------------------+                 +----------------------+
| Document Upload   |                 | Question Answering   |
+---------+---------+                 +----------+-----------+
          |                                       |
          v                                       v
+-------------------+                 +----------------------+
| PDF Reader        |                 | Spring AI Advisors   |
| Text Extraction   |                 | Chat Memory          |
+---------+---------+                 | RAG Retrieval        |
          |                           +----------+-----------+
          v                                      |
+-------------------+                           v
| Text Chunking     |               +-------------------------+
+---------+---------+               |      Ollama LLM         |
          |                         |      Qwen 2.5           |
          v                         +------------+------------+
+-------------------+                            |
| Embedding Model   |                            |
| nomic-embed-text  |                            |
+---------+---------+                            |
          |                                      |
          v                                      |
+-------------------+                            |
| PGVector          |<---------------------------+
| PostgreSQL        |
+-------------------+
```

---

# Key Features

### Document Upload

Users can upload PDF documents through REST APIs.

### Intelligent Chunking

Documents are split into smaller chunks before embedding generation, improving retrieval accuracy and reducing context noise.

### Embedding Generation

The application uses:

* **nomic-embed-text**
* Running locally through **Ollama**

to convert document chunks into vector embeddings.

### Vector Database Search

Embeddings are stored in:

* PostgreSQL
* PGVector extension

This enables semantic similarity search across document content.

### Retrieval-Augmented Generation (RAG)

When a user asks a question:

1. Relevant chunks are retrieved from PGVector.
2. Retrieved context is injected into the prompt.
3. The LLM generates a response using only the retrieved document information.

### Conversational Memory

The application maintains chat history using:

* Spring AI Chat Memory
* JDBC Memory Repository

allowing follow-up questions and conversational interactions.

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 3
* Spring AI
* Spring Data JPA
* Lombok

## AI & Machine Learning

* Spring AI
* Ollama
* Qwen 2.5 LLM
* nomic-embed-text Embeddings

## Database

* PostgreSQL
* PGVector

## Build Tool

* Maven

## API Testing

* Postman

---

# Spring AI Components Used

## ChatClient

Handles communication with local LLMs through Ollama.

## QuestionAnswerAdvisor

Responsible for Retrieval-Augmented Generation by:

* Retrieving relevant chunks
* Injecting context into prompts

## MessageChatMemoryAdvisor

Provides conversational memory across user sessions.

## VectorStore

Stores and retrieves vector embeddings from PGVector.

---

# RAG Workflow

## Step 1: Upload Document

User uploads a PDF document.

## Step 2: Extract Content

Text is extracted from the document.

## Step 3: Chunking

The content is split into smaller chunks.

## Step 4: Embedding Generation

Each chunk is converted into a vector embedding.

## Step 5: Storage

Embeddings are stored in PGVector.

## Step 6: Question Answering

User submits a question.

## Step 7: Retrieval

Relevant chunks are retrieved using semantic similarity search.

## Step 8: Response Generation

The LLM generates a grounded answer using retrieved context.

---

# Example Use Cases

* Interview Preparation Documents
* Technical Documentation Search
* Company Knowledge Bases
* Internal Documentation Assistants
* Research Papers
* Training Manuals
* AI-Powered Enterprise Search

---

# Future Enhancements

* Multi-document search
* Hybrid search (keyword + vector search)
* Citation support
* Streaming responses
* Authentication & authorization
* Multi-user workspaces
* Document summarization
* AI agents and tool calling
* Cloud deployment on Kubernetes

---

# Learning Outcomes

This project demonstrates practical experience with:

* Generative AI
* Retrieval-Augmented Generation (RAG)
* Large Language Models (LLMs)
* Spring AI
* Vector Databases
* Semantic Search
* Embedding Models
* PostgreSQL & PGVector
* Enterprise Java Development
* AI-powered backend systems

---

# Author

Umair Ali

