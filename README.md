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


Y pegar ahí el contenido del JSON enviado en el correo.

⚙️ 2️⃣ Configurar variables de entorno

Para ejecutar el proyecto correctamente, debe utilizar el archivo .env.local que fue enviado adjunto en el correo de entrega de la prueba técnica.

📌 Pasos:

Tomar el archivo .env.local recibido por correo.

Colocarlo en la raíz del proyecto (gpets), en la misma ubicación donde se encuentra el archivo docker-compose.yml.

La estructura debería verse así:

gpets/
│
├── docker-compose.yml
├── .env.local   ✅ (colocar aquí)
├── gpets-frontend/
└── gpets-backend/


Este archivo contiene todas las variables de entorno necesarias para que el frontend y backend funcionen correctamente.

🔐 Por motivos de seguridad, este archivo no se encuentra dentro del repositorio.