# 🐾 gpets-frontend

Frontend de la aplicación **gpets**, desarrollado con **React + TypeScript + Vite**, integrando autenticación con Firebase, mapas con Google Maps y consumo de API REST del backend.

---

## 🚀 Tecnologías utilizadas

- React 18
- TypeScript
- Vite
- TailwindCSS
- Firebase Authentication (Google Login)
- Firebase Realtime Database (listeners en tiempo real)
- Google Maps JavaScript API
- Docker (multi-stage build)
- Nginx (reverse proxy + SPA routing)

---

## 🧠 Arquitectura

Estructura principal del proyecto:

src/
auth/
components/
domain/
pages/
services/

- **auth/** → Configuración de Firebase y manejo de sesión.
- **domain/** → Modelos tipados (Pet, Owner, etc.).
- **services/** → Capa de comunicación HTTP con el backend.
- **pages/** → Vistas principales.
- **components/** → Componentes reutilizables.

El frontend no accede directamente a la base de datos para operaciones críticas.  
Toda la lógica de negocio pasa por el backend mediante API REST.

---

## 🔐 Autenticación

Se utiliza **Firebase Authentication con Google**.

En cada request protegida se envía automáticamente el ID Token:

