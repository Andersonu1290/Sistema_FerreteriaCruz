# 🛒 Sistema de Gestión SPA - Ferretería Cruz (Frontend)

![Vue.js](https://img.shields.io/badge/Vue.js_3-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-43853D?style=for-the-badge&logo=node.js&logoColor=white)

La interfaz de usuario interactiva y de alto rendimiento para el sistema de la Ferretería Cruz. Construida como una **Aplicación de Página Única (SPA)**, este proyecto representa la capa de presentación (Frontend) del ecosistema, consumiendo los servicios de la API REST del backend mediante peticiones asíncronas.

Diseñada para ofrecer una experiencia reactiva que elimina la necesidad de recargar la página del navegador, garantizando así tiempos de respuesta casi instantáneos (inferiores a 2 segundos) en el mostrador del Punto de Venta (POS) y una navegación fluida en la tienda pública.

---

## ✨ Características Principales

El frontend está dividido arquitectónicamente en dos grandes módulos, cada uno con su propio diseño (*Layout*):

### 🏭 Módulo Administrativo (Intranet / POS)
* **Punto de Venta Ágil (POS):** Interfaz optimizada para el registro de ventas físicas en el mostrador, con cálculo automático de totales e IGV, y lectura rápida de stock.
* **Gestión de Inventario y Kardex:** Pantallas interactivas para el control de ingresos, salidas, anulación de ventas y registro de mermas (productos dañados).
* **Panel de Reportes:** Visualización de estadísticas de ventas y descarga segura de reportes gerenciales en Excel.
* **Seguridad de Rutas:** Protección de vistas mediante validación de tokens JWT en el almacenamiento local.

### 🛍️ Módulo Público (E-commerce)
* **Catálogo Virtual:** Exhibición dinámica de productos con filtros por categoría en tiempo real.
* **Carrito de Compras:** Gestión del estado global del carrito de compras, persistente durante la navegación del usuario.
* **Checkout y Seguimiento:** Pasos de confirmación de compra y seguimiento de pedidos para clientes finales.
* **Chatbot de Soporte:** Widget integrado para asistencia automatizada básica.

---

## 💻 Tecnologías y Herramientas

* **Framework Progresivo:** Vue.js 3 (Composition API).
* **Entorno y Compilación:** Vite (Ofrece *Hot Module Replacement* ultrarrápido y empaquetado optimizado).
* **Enrutamiento:** Vue Router (Para la navegación SPA sin recargas).
* **Diseño y Maquetación:** Tailwind CSS (Framework de estilos utilitarios 100% responsivo).
* **Gestión de Estado:** Almacenamiento centralizado (`store`) para manejar globalmente la Autenticación (`auth.js`), el Carrito (`carrito.js`) y los Filtros (`filtros.js`).
* **Cliente HTTP:** Comunicación asíncrona estructurada a través de `services/apiClient.js` (Fetch/Axios) enviando y recibiendo objetos JSON.

---

## 📂 Estructura del Proyecto

El código fuente principal está organizado estratégicamente en el directorio `src/` para separar responsabilidades:

```text
src/
 ├── assets/          # Hojas de estilo globales (tailwind.css) e imágenes.
 ├── components/      # Componentes reutilizables (ChatbotWidget, ProductCard, Sidebar).
 ├── layouts/         # Plantillas maestras (AdminLayout y PublicLayout).
 ├── router/          # Configuración de las rutas web y protección (navigation guards).
 ├── services/        # Archivos de conexión (apiClient) para interactuar con Spring Boot.
 ├── store/           # Gestores de estado global en memoria del navegador.
 ├── views/           # Vistas principales renderizadas por Vue Router.
 │    ├── admin/      # Pantallas del panel de control (Ventas, Kardex, Inventario).
 │    └── public/     # Pantallas de la tienda virtual (Catálogo, Checkout, Perfil).
 ├── App.vue          # Componente raíz de Vue.
 └── main.js          # Punto de entrada y montaje de la aplicación.
