# 🐾 gpets-backend (API)

Backend de **gpets**, desarrollado en **Java 21 + Spring Boot**, con autenticación mediante **Firebase Auth** y persistencia en **Firebase Realtime Database**.  
La API centraliza la lógica de negocio y actúa como capa de seguridad/validación entre el frontend y Firebase.

---

## 🚀 Tecnologías

- Java 21
- Spring Boot 4
- Spring Security
- Firebase Admin SDK
- Firebase Realtime Database
- Springdoc OpenAPI (Swagger)
- Docker (multi-stage build)

---

## 🧠 Arquitectura / Capas

El proyecto está organizado siguiendo una separación por responsabilidades:

src/main/java/com/gpets/gpetsapi/
application/
domain/
controller/
dto/
infrastructure/
security/
config/


### ✅ `domain/`
Contiene el núcleo del negocio:
- Entidades y modelos (ej. `Pet`, `Owner`)
- Value Objects
- Reglas y validaciones de dominio
- Contratos (interfaces) como `PetsRepository`

### ✅ `application/`
Casos de uso / servicios de aplicación:
- Orquesta el flujo entre dominio e infraestructura
- Aplica reglas de negocio
- Ej.: `PetsService` para listar, detalle, actualizar ubicación, claim, etc.

### ✅ `infrastructure/`
Implementaciones técnicas:
- Repositorios en Firebase (Realtime Database)
- Mappers (`Record` ↔ `Domain`)
- Acceso a Firebase mediante Admin SDK

### ✅ `controller/`
Exposición REST:
- Endpoints HTTP (`/api/pets`, `/api/owners`, etc.)
- Llama a la capa `application`
- Retorna DTOs (no expone directamente el dominio)

### ✅ `dto/`
Modelos de entrada/salida:
- Requests y Responses (ej. `LocationUpdateRequest`)
- `DtoMapper` para mapear a formatos de API

### ✅ `security/`
Seguridad y autenticación:
- Filtro `FirebaseAuthFilter` para validar el token Bearer
- Rutas protegidas (requieren usuario autenticado)

### ✅ `config/`
Configuración de Firebase:
- Inicializa Firebase Admin SDK leyendo credenciales desde variables de entorno / properties

---

## 🔐 Autenticación (Firebase)

El frontend envía el ID Token de Firebase en cada request protegida:

El backend valida el token usando Firebase Admin SDK y establece el contexto de seguridad.

---

## 🌍 Variables de entorno (recomendado)

El backend se configura por variables de entorno para ser portable (Docker/Cloud):

- `FIREBASE_CREDENTIALS_PATH` → ruta del service account JSON dentro del contenedor
- `FIREBASE_DATABASE_URL` → URL de tu Realtime Database

En `application.properties` se usa:

```properties
firebase.databaseUrl=${FIREBASE_DATABASE_URL:}
firebase.credentialsPath=${FIREBASE_CREDENTIALS_PATH:}

🔑 firebase-admin.json (Service Account)

Este proyecto requiere un archivo de credenciales Firebase Admin para que el backend pueda:

Verificar tokens de Firebase Auth

Leer/escribir en Firebase Realtime Database como servidor

📌 Este archivo NO se sube al repositorio por seguridad.

🔐 Configuración de Firebase Admin

Por motivos de seguridad, el archivo de credenciales firebase-admin.json no se encuentra dentro del repositorio.
Adjunto dicho archivo en este correo.

Para que el backend funcione correctamente, debe colocarlo en la siguiente ruta dentro del proyecto:

gpets-backend/secrets/firebase-admin.json

(Si la carpeta secrets no existiera, puede crearla manualmente).

Una vez ubicado el archivo en esa ruta, el backend reconocerá automáticamente las credenciales al iniciar con Docker.
