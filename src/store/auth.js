import { reactive, watch } from 'vue';
import { esJwtExpirado } from '@/utils/auth';

// 1. Recuperamos la sesión si existe al recargar la página
const sesionGuardada = localStorage.getItem('clienteEstrella');
let clienteInicial = null;

if (sesionGuardada) {
  try {
    const parsed = JSON.parse(sesionGuardada);
    
    // MAGIA DE SEGURIDAD: Verificamos si el token ya expiró en este exacto momento
    if (parsed.token && esJwtExpirado(parsed.token)) {
      console.warn("Seguridad: El token JWT ha expirado. Limpiando sesión automáticamente.");
      localStorage.removeItem('clienteEstrella');
    } else {
      clienteInicial = parsed;
    }
  } catch (e) {
    localStorage.removeItem('clienteEstrella');
  }
}

export const authStore = reactive({
  usuarioActual: clienteInicial,
  
  get estaLogueado() {
    // Doble verificación reactiva: Está logueado SOLO si existe el usuario Y el token está vivo
    return this.usuarioActual !== null && !esJwtExpirado(this.usuarioActual.token);
  },
  
  iniciarSesion(datosUsuario) {
    this.usuarioActual = datosUsuario;
  },
  
  cerrarSesion() {
    this.usuarioActual = null;
    localStorage.removeItem('clienteEstrella');
  }
});

// Observador: Guarda automáticamente en localStorage si el usuario cambia validamente
watch(
  () => authStore.usuarioActual,
  (nuevoUsuario) => {
    if (nuevoUsuario) {
      localStorage.setItem('clienteEstrella', JSON.stringify(nuevoUsuario));
    } else {
      localStorage.removeItem('clienteEstrella');
    }
  },
  { deep: true }
);