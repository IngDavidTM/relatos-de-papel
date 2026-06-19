<h1 align="center">📚 Relatos de Papel</h1>

<p align="center">
  Librería online full-stack: SPA en React + back-end de microservicios con Java y Spring Cloud.
</p>

---

## Descripción

**Relatos de Papel** es una aplicación web de comercio electrónico de libros. Permite
explorar un catálogo, gestionar un carrito, registrarse e iniciar sesión, realizar
compras y conversar con un asistente de soporte basado en IA. La arquitectura sigue
un patrón de **microservicios** con descubrimiento de servicios, API Gateway,
autenticación mediante *phantom token*, comunicación asíncrona por eventos y WebSockets.

## Arquitectura

```
                React SPA  ───────────────▶  API Gateway (8080)
                                                  │  valida token opaco e inyecta JWT
                                                  ▼
                                          Eureka (descubrimiento)
        ┌──────────────┬──────────────┬───────────────┬───────────────┐
   catalogue (8081)  orders (8082)  users (8083)   comms (8084)
        │              │              │               │
      MySQL          MySQL        MySQL + Redis    RabbitMQ + SMTP + Gemini
                       └──── evento "order.created" ──▶ comms ──▶ email
```

| Componente | Puerto | Tecnología | Responsabilidad |
|---|---|---|---|
| **frontend** | 5173 | React 19 + Vite | SPA: catálogo, carrito, auth, perfil, chat de soporte |
| **eureka-server** | 8761 | Spring Cloud Netflix Eureka | Descubrimiento de servicios |
| **api-gateway** | 8080 | Spring Cloud Gateway | Punto de entrada único, seguridad perimetral (phantom token) |
| **catalogue-service** | 8081 | Spring Boot + JPA + MySQL | Catálogo de libros |
| **orders-service** | 8082 | Spring Boot + JPA + RabbitMQ | Pedidos y emisión de eventos |
| **users-service** | 8083 | Spring Boot + JPA + Redis | Autenticación (JWT + token opaco) y perfiles |
| **comms-service** | 8084 | Spring Boot + AMQP + WebSocket | Email asíncrono y chat de soporte con IA (Gemini) |

## Stack tecnológico

- **Front-end:** React 19, React Router 7, Vite, WebSocket nativo.
- **Back-end:** Java 21, Spring Boot 3.3, Spring Cloud 2023, Spring Data JPA.
- **Datos / mensajería:** MySQL 8, Redis 7, RabbitMQ 3.
- **Seguridad:** *phantom token* (JWT firmado + token opaco con TTL en Redis), BCrypt.
- **IA / mensajería externa:** Google Gemini API, SMTP (Gmail).
- **Infraestructura:** Docker y Docker Compose.

## Puesta en marcha

### Back-end (Docker)

```bash
cd backend
cp .env.example .env        # opcional: rellena credenciales de email e IA
docker compose up --build -d
```

- Eureka: <http://localhost:8761>
- Rutas del Gateway: <http://localhost:8080/actuator/gateway/routes>
- RabbitMQ: <http://localhost:15672> (credenciales de `backend/.env`)

Más detalles en [`backend/README.md`](backend/README.md).

### Front-end

```bash
cd frontend
npm install
cp .env.example .env        # VITE_API_URL apuntando al Gateway
npm run dev
```

La SPA quedará disponible en <http://localhost:5173>.

## Estructura del repositorio

```
relatos-de-papel/
├── frontend/        SPA en React
└── backend/         Microservicios, scripts SQL y docker-compose
```

## Despliegue

- **Front-end:** Vercel (build de Vite, raíz `frontend/`).
- **Back-end:** Railway (un servicio por carpeta + MySQL, Redis y RabbitMQ gestionados).

La configuración, variables y comprobaciones necesarias están detalladas en
[`DEPLOYMENT.md`](DEPLOYMENT.md).

## Licencia

Distribuido bajo los términos de la licencia incluida en [`LICENSE`](LICENSE).
