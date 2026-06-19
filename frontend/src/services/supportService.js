// Conexión WebSocket al chat de soporte (microservicio comms vía Gateway).

const WS_URL = import.meta.env.VITE_WS_URL || 'ws://localhost:8080';

export function connectSupport({ onMessage, onOpen, onClose, onError } = {}) {
  const socket = new WebSocket(`${WS_URL}/ws/support`);

  if (onOpen) socket.addEventListener('open', onOpen);
  if (onClose) socket.addEventListener('close', onClose);
  if (onError) socket.addEventListener('error', onError);
  if (onMessage) {
    socket.addEventListener('message', (event) => onMessage(event.data));
  }

  return {
    send: (text) => {
      if (socket.readyState === WebSocket.OPEN) {
        socket.send(text);
      }
    },
    close: () => socket.close(),
  };
}
