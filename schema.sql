-- Habilitar la extensión pgvector.
CREATE EXTENSION IF NOT EXISTS vector;

-- Tabla del almacén vectorial que usa Spring AI.
CREATE TABLE IF NOT EXISTS public.vector_store (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(3072)
);