# Relatos de Papel — Back-end (microservicios)

Arquitectura de microservicios con Java 21 + Spring Boot 3.3 / Spring Cloud 2023.

## Componentes

| Servicio | Puerto | Descripción |
|---|---|---|
| `eureka-server` | 8761 | Servidor de descubrimiento (Netflix Eureka) |
| `api-gateway` | 8080 | Punto de entrada único. Transcribe POST→verbo real y valida el phantom token (defensor perimetral) |
| `catalogue-service` | 8081 | Catálogo de libros (MySQL + JPA) |
| `orders-service` | 8082 | Pedidos. Publica el evento `order.created` (RabbitMQ) |
| `users-service` | 8083 | Autenticación phantom token (JWT + opaco en Redis) y perfiles |
| `comms-service` | 8084 | Consume eventos → email (SMTP). WebSocket de soporte con IA (Gemini) |

Infraestructura: **MySQL 8**, **Redis 7**, **RabbitMQ 3**.

## Flujo de autenticación (phantom token)

1. El cliente hace `POST /api/auth/tokens` (login) y recibe un **token opaco**.
2. En cada petición protegida envía `Authorization: Bearer <opaco>`.
3. El **Gateway** valida el opaco contra `users-service`; si es válido, inyecta el
   **JWT** asociado como cabecera `accessToken` hacia el microservicio destino.
4. Endpoints públicos (login, registro, `GET` de catálogo, WebSocket de soporte)
   no requieren token.

## Despliegue local con Docker

Requisitos: Docker y Docker Compose.

```bash
# 1. (Opcional) configurar secretos para email e IA
cp .env.example .env   # y rellena GMAIL_* y GEMINI_API_KEY

# 2. Construir y levantar toda la arquitectura
docker compose up --build -d

# 3. Ver el estado
docker compose ps
```

- Dashboard de Eureka: <http://localhost:8761>
- Rutas del Gateway: <http://localhost:8080/actuator/gateway/routes>
- Consola de RabbitMQ: <http://localhost:15672> (credenciales del archivo `.env`)
- Las bases de datos se inicializan automáticamente con los scripts de `./sql`
  (120 libros de ejemplo, usuarios demo).

Para detener:

```bash
docker compose down          # conserva los datos
docker compose down -v       # elimina también los volúmenes (MySQL)
```

> El **front-end** se ejecuta de forma local (no en contenedor), apuntando al
> Gateway mediante `VITE_API_URL=http://localhost:8080`.

## Ejecución nativa (sin Docker)

Necesitas MySQL, Redis y RabbitMQ en local y un JDK 21. Ejecuta el SQL de `./sql`
y arranca cada módulo con `mvn spring-boot:run` en el orden: eureka → gateway →
catalogue → orders → users → comms.

## Usuarios demo

| Email | Contraseña | Rol |
|---|---|---|
| claudia.rivera@relatosdepapel.com | relatos123 | ROLE_USER |
| admin@relatosdepapel.com | admin123 | ROLE_ADMIN |

> Estas cuentas son exclusivamente de desarrollo. No cargues `users_dml.sql` en
> un entorno público. Consulta [`../DEPLOYMENT.md`](../DEPLOYMENT.md) para el
> despliegue de producción.
