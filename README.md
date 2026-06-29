# DocuChat

A full-stack **Retrieval-Augmented Generation (RAG)** web application. Users paste document content, the app generates vector embeddings and stores them in a vector database. On each question, it retrieves the most semantically relevant chunks and feeds them as context to an LLM to produce accurate, document-grounded answers.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-latest-green)](https://spring.io/projects/spring-ai)
[![React](https://img.shields.io/badge/React-19-blue)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17%20+%20pgvector-336791)](https://github.com/pgvector/pgvector)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED)](https://www.docker.com/)

---

## How it works

The RAG pipeline runs in two phases:

**Ingestion**
Document text → Chunking → Gemini text-embedding-004 → pgvector storage

**Query**
User question → Question embedding → Cosine similarity search → Relevant chunks → Enriched prompt → Gemini 2.0 Flash → Answer

Semantic search uses cosine similarity over the stored vectors, so results are based on meaning rather than exact keyword matches.

---

## Tech Stack

**Backend**
- Java 21, Spring Boot 3.5
- Spring AI (embedding pipeline + LLM integration)
- Spring Data JPA, Spring Web

**AI Models**
- Google Gemini 2.0 Flash — answer generation
- text-embedding-004 — vector embeddings (768 dimensions)

**Database**
- PostgreSQL 17 with pgvector extension

**Frontend**
- React + TypeScript + Vite

**Infrastructure**
- Docker + Docker Compose

---

## Technical Highlights

- **Spring AI abstraction layer.** The embedding and chat client are injected via Spring AI's unified API, keeping the AI provider swappable without changing business logic.
- **pgvector cosine similarity search.** Chunk retrieval is a single SQL query using the `<=>` operator, offloading vector math to the database layer.
- **Chunk-based ingestion.** Documents are split into fixed-size chunks before embedding, enabling precise retrieval at the paragraph level rather than the full document.
- **Stateless REST API.** The backend exposes two endpoints: one for ingestion, one for querying. No sessions, no state.

---

## Project Structure
docuchat/

├── src/main/java/com/josemanuel/docuchat/

│   ├── controller/      # REST endpoints

│   ├── service/         # RAG pipeline logic

│   └── config/          # Spring AI and pgvector config

├── frontend/

│   └── src/

│       ├── components/  # React components

│       └── App.tsx

├── docker-compose.yml   # pgvector container

├── Dockerfile           # Backend build

└── schema.sql           # Embeddings table schema

---

## Running Locally

**Prerequisites:** Docker, Java 21, Node.js 18+

```bash
# 1. Clone the repository
git clone https://github.com/josemanueldg02-star/docuchat
cd docuchat

# 2. Create a .env file in the root
GEMINI_API_KEY=your_google_ai_studio_key
POSTGRES_PASSWORD=your_password

# 3. Start the database
docker compose up -d

# 4. Start the backend (http://localhost:8080)
./mvnw spring-boot:run

# 5. Start the frontend (http://localhost:5173)
cd frontend && npm install && npm run dev
```

---

## Known Limitation

The free tier of the Gemini API blocks requests from cloud datacenter IPs (Render, AWS, GCP, etc.), which prevents a live deployment on free-tier infrastructure. The application works correctly in a local environment.

**Evaluated alternatives to resolve the restriction:**
- **Mistral AI** — `mistral-embed` via `spring-ai-starter-model-mistral-ai`, no IP restriction on free tier, 1024 dimensions
- **Cohere** — `embed-multilingual-v3.0` with native Spring AI support, optimal for multilingual documents

Both options would require migrating the pgvector column from 768 to 1024 dimensions and re-embedding existing documents.

---

## Author

**José Manuel Domínguez García**
- GitHub: [@josemanueldg02-star](https://github.com/josemanueldg02-star)└── schema.sql                   # Esquema de la tabla de embeddings
```

## Ejecutar localmente

**Requisitos previos:** Docker, Java 21, Node.js 18+

**1. Clonar el repositorio**

```bash
git clone https://github.com/josemanueldg02-star/docuchat
cd docuchat
```

**2. Configurar variables de entorno**

Crear un archivo `.env` en la raíz con:

```
GEMINI_API_KEY=tu_api_key_de_google_ai_studio
POSTGRES_PASSWORD=tu_password
```

**3. Arrancar la base de datos**

```bash
docker compose up -d
```

**4. Arrancar el backend**

```bash
./mvnw spring-boot:run
```

**5. Arrancar el frontend**

```bash
cd frontend
npm install
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`. El backend corre en `http://localhost:8080`.

## Limitación conocida en producción

La API gratuita de Gemini bloquea requests originadas desde IPs de datacenter cloud (Render, AWS, GCP, etc.), lo cual impide el deployment funcional en free tier. El proyecto funciona correctamente en entorno local.

Alternativas técnicas evaluadas para resolver el bloqueo:
- **Mistral AI** — `mistral-embed` con `spring-ai-starter-model-mistral-ai`, free tier sin restricción de IP, 1024 dimensiones
- **Cohere** — `embed-multilingual-v3.0` con soporte nativo en Spring AI, óptimo para documentos en múltiples idiomas

Ambas opciones requieren migrar la columna de pgvector de 768 a 1024 dimensiones y re-embeber los documentos existentes.
