import { useEffect, useRef, useState } from 'react';
import { connectSupport } from '../services/supportService';

// Widget flotante de atención al cliente conectado por WebSocket al
// microservicio comms (agente de IA Gemini) a través del Gateway.
function SupportChat() {
  const [open, setOpen] = useState(false);
  const [connected, setConnected] = useState(false);
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState('');
  const clientRef = useRef(null);

  useEffect(() => {
    if (!open || clientRef.current) {
      return undefined;
    }

    const client = connectSupport({
      onOpen: () => setConnected(true),
      onClose: () => setConnected(false),
      onError: () => setConnected(false),
      onMessage: (text) =>
        setMessages((prev) => [...prev, { from: 'agent', text }]),
    });
    clientRef.current = client;

    return () => {
      client.close();
      clientRef.current = null;
      setConnected(false);
    };
  }, [open]);

  const handleSend = (event) => {
    event.preventDefault();
    const text = draft.trim();
    if (!text || !clientRef.current) {
      return;
    }
    setMessages((prev) => [...prev, { from: 'user', text }]);
    clientRef.current.send(text);
    setDraft('');
  };

  return (
    <div className="support-chat">
      {open && (
        <div className="support-panel">
          <div className="support-header">
            <strong>Papelito · Soporte</strong>
            <span className={connected ? 'support-dot on' : 'support-dot'} />
            <button className="support-close" onClick={() => setOpen(false)} aria-label="Cerrar chat">
              ×
            </button>
          </div>

          <div className="support-messages">
            {messages.map((msg, index) => (
              <p key={index} className={`support-msg ${msg.from}`}>
                {msg.text}
              </p>
            ))}
          </div>

          <form className="support-input" onSubmit={handleSend}>
            <input
              type="text"
              value={draft}
              placeholder="Escribe tu consulta…"
              onChange={(event) => setDraft(event.target.value)}
            />
            <button type="submit" className="btn btn-primary">
              Enviar
            </button>
          </form>
        </div>
      )}

      <button className="support-fab" onClick={() => setOpen((prev) => !prev)}>
        {open ? 'Cerrar' : '💬 Soporte'}
      </button>
    </div>
  );
}

export default SupportChat;
