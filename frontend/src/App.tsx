import { useState } from "react";
import "./App.css";

const API_URL = "http://localhost:8080";

function App() {
  // Estado para la ingesta
  const [documentText, setDocumentText] = useState("");
  const [sourceName, setSourceName] = useState("");
  const [ingestStatus, setIngestStatus] = useState("");

  // Estado para el chat
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [loading, setLoading] = useState(false);

  // Llamada al endpoint de ingesta
  async function handleIngest() {
    setIngestStatus("Ingiriendo...");
    try {
      const res = await fetch(`${API_URL}/api/ingest`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ text: documentText, sourceName: sourceName }),
      });
      const data = await res.json();
      setIngestStatus(`✅ ${data.message} (${data.chunksCreated} fragmentos)`);
      setDocumentText("");
      setSourceName("");
    } catch (err) {
      setIngestStatus("❌ Error al ingerir el documento");
    }
  }

  // Llamada al endpoint de chat
  async function handleAsk() {
    setLoading(true);
    setAnswer("");
    try {
      const res = await fetch(`${API_URL}/api/chat`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ question: question }),
      });
      const data = await res.json();
      setAnswer(data.answer);
    } catch (err) {
      setAnswer("❌ Error al obtener respuesta");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="container">
      <h1>DocuChat</h1>
      <p className="subtitle">Chatea con tus documentos usando RAG</p>

      {/* Sección de ingesta */}
      <section className="card">
        <h2>1. Ingerir documento</h2>
        <input
          type="text"
          placeholder="Nombre del documento (ej. apuntes-tema1)"
          value={sourceName}
          onChange={(e) => setSourceName(e.target.value)}
        />
        <textarea
          placeholder="Pega aquí el texto del documento..."
          value={documentText}
          onChange={(e) => setDocumentText(e.target.value)}
          rows={6}
        />
        <button onClick={handleIngest} disabled={!documentText}>
          Ingerir
        </button>
        {ingestStatus && <p className="status">{ingestStatus}</p>}
      </section>

      {/* Sección de chat */}
      <section className="card">
        <h2>2. Preguntar</h2>
        <input
          type="text"
          placeholder="Escribe tu pregunta..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleAsk()}
        />
        <button onClick={handleAsk} disabled={!question || loading}>
          {loading ? "Pensando..." : "Preguntar"}
        </button>
        {answer && (
          <div className="answer">
            <strong>Respuesta:</strong>
            <p>{answer}</p>
          </div>
        )}
      </section>
    </div>
  );
}

export default App;