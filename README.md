# 🐾 gpets – Reto Técnico

Proyecto fullstack compuesto por:

- 🖥 **Frontend**: React + TypeScript + Vite + Tailwind + Firebase Auth  
- ⚙️ **Backend**: Java 21 + Spring Boot + Firebase Admin SDK  
- 🗄 **Base de datos**: Firebase Realtime Database  
- 🐳 **Infraestructura**: Docker + Docker Compose  

El backend centraliza la lógica de negocio y validación, mientras que el frontend consume la API y maneja autenticación con Google.

---

# 🚀 Cómo ejecutar el proyecto (recomendado: Docker)

La forma correcta de correr todo el sistema es usando:

```bash
docker compose up --build

🔐 1️⃣ Configurar Firebase Admin (OBLIGATORIO)

El backend necesita un Service Account JSON para:

Validar tokens de Firebase Auth

Leer/escribir en Firebase Realtime Database

Este archivo NO está en el repositorio por seguridad.

Paso 1 – Descargar el Service Account

Ir a 👉 https://console.firebase.google.com

Abrir tu proyecto

⚙️ Project Settings

Ir a la pestaña Service Accounts

Click en Generate new private key

Descargar el archivo JSON

Paso 2 – Crear carpeta de secrets

En la raíz del repo:

gpets/
 ├── gpets-backend/
 ├── gpets-frontend/
 ├── docker-compose.yml
 └── .env


Dentro de gpets-backend/, crear:

gpets-backend/secrets/firebase-admin.json


Y pegar ahí el contenido del JSON descargado.

⚙️ 2️⃣ Configurar variables de entorno

En la raíz del repo existe un archivo:

.env


Debe contener algo como:

FIREBASE_DATABASE_URL=https://<tu-proyecto>-default-rtdb.firebaseio.com
VITE_API_BASE_URL=/api
VITE_GOOGLE_MAPS_API_KEY=TU_GOOGLE_MAPS_KEY
VITE_FIREBASE_API_KEY=...
VITE_FIREBASE_AUTH_DOMAIN=...
VITE_FIREBASE_PROJECT_ID=...
VITE_FIREBASE_STORAGE_BUCKET=...
VITE_FIREBASE_MESSAGING_SENDER_ID=...
VITE_FIREBASE_APP_ID=...