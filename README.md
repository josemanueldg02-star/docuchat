# DocuChat

Aplicación web de preguntas y respuestas sobre documentos basada en Retrieval-Augmented Generation (RAG). El usuario ingresa el contenido de un documento, la aplicación genera embeddings vectoriales y los almacena en una base de datos vectorial. Al hacer una pregunta, recupera los fragmentos más relevantes mediante búsqueda semántica y los usa como contexto para generar una respuesta precisa con un LLM.

## Stack tecnológico

**Backend:** Java 21 · Spring Boot 3.5 · Spring AI · Spring Web · Spring Data JPA  
**Inteligencia Artificial:** Google Gemini 2.0 Flash (generación) · text-embedding-004 (embeddings)  
**Base de datos:** PostgreSQL 17 con extensión pgvector  
**Frontend:** React · Vite · TypeScript · CSS  
**Infraestructura:** Docker · Docker Compose  

## Arquitectura

El flujo RAG se divide en dos fases:

**Ingesta de documentos**

Texto → Tokenización en chunks → Generación de embeddings (Gemini) → Almacenamiento en pgvector

**Consulta**

Pregunta del usuario → Embedding de la pregunta → Búsqueda semántica en pgvector → Recuperación de chunks relevantes → Prompt enriquecido → Gemini → Respuesta con contexto

La búsqueda semántica usa similitud coseno sobre los vectores almacenados en pgvector para recuperar los fragmentos del documento más relevantes para cada pregunta, independientemente de si comparten palabras exactas.

## Estructura del proyecto

```
docuchat/
├── src/
│   └── main/
│       ├── java/com/josemanuel/docuchat/
│       │   ├── controller/      # Endpoints REST
│       │   ├── service/         # Lógica RAG
│       │   └── config/          # Configuración Spring AI y pgvector
│       └── resources/
│           └── application.properties
├── frontend/
│   └── src/
│       ├── components/          # Componentes React
│       └── App.tsx
├── docker-compose.yml           # pgvector en contenedor
├── Dockerfile                   # Build del backend
└── schema.sql                   # Esquema de la tabla de embeddings
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