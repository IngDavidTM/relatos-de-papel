# Despliegue de producción: Railway + Vercel

La arquitectura se despliega como servicios independientes. Solo el API Gateway
debe tener dominio público en Railway; Eureka y los microservicios permanecen en
la red privada del proyecto.

## 1. Servicios en Railway

Crea un proyecto y añade:

- MySQL, Redis y RabbitMQ.
- `eureka-server`, `api-gateway`, `catalogue-service`, `orders-service`,
  `users-service` y `comms-service`, todos desde este repositorio.

Configura el **Root Directory** de cada servicio con su carpeta correspondiente,
por ejemplo `backend/api-gateway`. Cada carpeta contiene un `Dockerfile` y un
`railway.json`.

Genera dominio público únicamente para `api-gateway`. Los demás servicios deben
comunicarse mediante dominios privados o Eureka y no deben aceptar tráfico de
Internet directamente.

## 2. Variables de Railway

Usa variables de referencia de Railway cuando el valor proceda de otro servicio.
Los nombres concretos de las referencias dependen de los nombres asignados en el
proyecto.

### RabbitMQ

Configura estas variables en el contenedor del broker y reutiliza los mismos
valores en Orders y Comms:

```text
RABBITMQ_DEFAULT_USER=<usuario-no-guest>
RABBITMQ_DEFAULT_PASS=<contraseña-segura>
```

### Eureka

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_HOSTNAME=<dominio-privado-de-eureka>
```

### API Gateway

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_URL=http://<dominio-privado-de-eureka>:<puerto>/eureka/
GATEWAY_CORS_ALLOWED_ORIGINS=https://<dominio-vercel>
```

### Catalogue

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_URL=http://<dominio-privado-de-eureka>:<puerto>/eureka/
DB_HOST=<host-privado-mysql>
DB_PORT=<puerto-mysql>
DB_USERNAME=<usuario-mysql>
DB_PASSWORD=<contraseña-mysql>
```

### Orders

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_URL=http://<dominio-privado-de-eureka>:<puerto>/eureka/
DB_HOST=<host-privado-mysql>
DB_PORT=<puerto-mysql>
DB_USERNAME=<usuario-mysql>
DB_PASSWORD=<contraseña-mysql>
RABBITMQ_HOST=<dominio-privado-rabbitmq>
RABBITMQ_PORT=<puerto-amqp>
RABBITMQ_USERNAME=<usuario-no-guest>
RABBITMQ_PASSWORD=<contraseña-segura>
```

### Users

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_URL=http://<dominio-privado-de-eureka>:<puerto>/eureka/
DB_HOST=<host-privado-mysql>
DB_PORT=<puerto-mysql>
DB_USERNAME=<usuario-mysql>
DB_PASSWORD=<contraseña-mysql>
REDIS_HOST=<dominio-privado-redis>
REDIS_PORT=<puerto-redis>
REDIS_PASSWORD=<contraseña-redis-si-aplica>
JWT_SECRET=<secreto-aleatorio-de-al-menos-32-bytes>
JWT_EXPIRATION=3600
```

### Comms

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_URL=http://<dominio-privado-de-eureka>:<puerto>/eureka/
RABBITMQ_HOST=<dominio-privado-rabbitmq>
RABBITMQ_PORT=<puerto-amqp>
RABBITMQ_USERNAME=<usuario-no-guest>
RABBITMQ_PASSWORD=<contraseña-segura>
GMAIL_USERNAME=<cuenta-gmail>
GMAIL_APP_PASSWORD=<app-password-de-google>
GEMINI_API_KEY=<api-key>
GEMINI_MODEL=gemini-2.5-flash
CORS_ALLOWED_ORIGINS=https://<dominio-vercel>
```

Railway proporciona `PORT`; todos los servicios lo consumen automáticamente.
No uses los valores por defecto incluidos para desarrollo local como secretos de
producción.

## 3. Inicialización de MySQL

El volumen `docker-entrypoint-initdb.d` solo existe en Docker Compose. En Railway
hay que ejecutar manualmente, con el usuario administrado de MySQL y en este orden:

1. `backend/sql/catalogue_ddl.sql`
2. `backend/sql/orders_ddl.sql`
3. `backend/sql/users_ddl.sql`
4. `backend/sql/catalogue_dml.sql` si se desea cargar el catálogo de demostración.

No ejecutes `users_dml.sql` en producción: contiene usuarios de demostración con
credenciales conocidas. Registra el usuario administrador con una contraseña
segura y cambia su rol a `ROLE_ADMIN` directamente en `users_db.users`.

## 4. Front-end en Vercel

Importa el repositorio y establece `frontend` como **Root Directory**. Configura:

```text
VITE_API_URL=https://<dominio-publico-del-gateway>
VITE_WS_URL=wss://<dominio-publico-del-gateway>
```

El archivo `frontend/vercel.json` redirige las rutas de React a `index.html` para
que enlaces directos como `/profile` no devuelvan 404.

Cuando Vercel asigne el dominio definitivo, actualiza
`GATEWAY_CORS_ALLOWED_ORIGINS` y `CORS_ALLOWED_ORIGINS` en Railway y vuelve a
desplegar Gateway y Comms.

## 5. Verificación mínima

1. Comprueba que catalogue, orders, users, comms y gateway aparecen `UP` en Eureka.
2. Abre `https://<gateway>/actuator/health` y verifica `UP`.
3. Registra un usuario, inicia sesión y confirma que el navegador recibe solo el
   token opaco.
4. Crea un pedido y verifica la recepción del evento y del correo.
5. Abre el chat desde el dominio Vercel y confirma la conexión `wss` y la respuesta
   de Gemini.
6. Confirma que `/api/auth/tokens/validate` devuelve 403 desde Internet.
7. Confirma que un usuario no puede consultar pedidos o perfiles de otro usuario.

Si falla el envío de correo después de los reintentos, el evento queda en la cola
`comms.order-created.dlq` para inspección y reproceso.
