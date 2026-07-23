<template>
  <div class="min-h-[85vh] flex items-center justify-center py-8 px-4 sm:px-6 lg:px-8 bg-slate-50 relative overflow-hidden">
    
    <div class="absolute top-[-10%] left-[-10%] w-96 h-96 bg-blue-400/20 rounded-full mix-blend-multiply filter blur-3xl opacity-70 animate-blob"></div>
    <div class="absolute bottom-[-10%] right-[-10%] w-96 h-96 bg-medical-blue/20 rounded-full mix-blend-multiply filter blur-3xl opacity-70 animate-blob animation-delay-2000"></div>

    <div class="max-w-6xl w-full bg-white rounded-[2.5rem] shadow-2xl overflow-hidden flex flex-col-reverse md:flex-row border border-slate-100 relative z-10">
      
      <!-- Lado Izquierdo: Formulario -->
      <div class="w-full md:w-1/2 p-8 sm:p-12 lg:p-16 bg-white relative overflow-y-auto max-h-[85vh] custom-scrollbar">
        
        <div class="flex items-center gap-4 mb-10 border-b border-slate-200 pb-4">
          <button 
            @click="esRegistro = false; limpiarErrores()" 
            class="text-lg font-black transition-colors relative pb-4 -mb-4"
            :class="!esRegistro ? 'text-medical-blue' : 'text-slate-400 hover:text-slate-600'"
          >
            Iniciar Sesión
            <div v-if="!esRegistro" class="absolute bottom-0 left-0 w-full h-1 bg-medical-blue rounded-t-lg"></div>
          </button>
          <button 
            @click="esRegistro = true; limpiarErrores()" 
            class="text-lg font-black transition-colors relative pb-4 -mb-4"
            :class="esRegistro ? 'text-medical-blue' : 'text-slate-400 hover:text-slate-600'"
          >
            Crear Cuenta
            <div v-if="esRegistro" class="absolute bottom-0 left-0 w-full h-1 bg-medical-blue rounded-t-lg"></div>
          </button>
        </div>

        <div class="mb-8">
          <h1 class="text-3xl font-black text-slate-800 tracking-tight">
            {{ esRegistro ? 'Únete a nosotros' : 'Bienvenido de nuevo' }}
          </h1>
          <p class="text-slate-500 mt-2 text-sm font-medium">
            {{ esRegistro ? 'Completa tus datos personales para unirte a Ferretería Cruz.' : 'Ingresa tus credenciales para continuar con tus compras.' }}
          </p>
        </div>

        <form @submit.prevent="procesarFormulario" class="space-y-5">
          
          <!-- ============================== -->
          <!-- CAMPOS EXCLUSIVOS DE REGISTRO  -->
          <!-- ============================== -->
          <div v-if="esRegistro" class="grid grid-cols-1 md:grid-cols-2 gap-5 animate-fade-in">
            <!-- DNI -->
            <div>
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">DNI / RUC</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V8a2 2 0 00-2-2h-5m-4 0V5a2 2 0 114 0v1m-4 0a2 2 0 104 0m-5 8a2 2 0 100-4 2 2 0 000 4zm0 0c1.306 0 2.417.835 2.83 2M9 14a3.001 3.001 0 00-2.83 2M15 11h3m-3 4h2" /></svg>
                </div>
                <input 
                  v-model="form.dni" 
                  type="text" 
                  required 
                  maxlength="11"
                  placeholder="Ej. 70123456" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                >
              </div>
            </div>

            <!-- Usuario (Username) -->
            <div>
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Nombre de Usuario</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                </div>
                <input 
                  v-model="form.username" 
                  type="text" 
                  required 
                  placeholder="Ej. jperez" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                >
              </div>
            </div>

            <!-- Nombre -->
            <div>
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Nombres</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                </div>
                <input 
                  v-model="form.nombre" 
                  type="text" 
                  required 
                  placeholder="Ej. Juan" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                >
              </div>
            </div>

            <!-- Apellido -->
            <div>
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Apellidos</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                </div>
                <input 
                  v-model="form.apellido" 
                  type="text" 
                  required 
                  placeholder="Ej. Pérez" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                >
              </div>
            </div>

            <!-- Correo Electrónico -->
            <div class="md:col-span-2">
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Correo Electrónico</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207" /></svg>
                </div>
                <input 
                  v-model="form.correo" 
                  type="email" 
                  required 
                  placeholder="tu@correo.com" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                  :class="{'border-red-300 bg-red-50': form.correo && !esCorreoValido}"
                >
              </div>
              <p v-if="form.correo && !esCorreoValido" class="text-[10px] text-red-500 mt-1.5 font-bold">Ingresa un correo válido (ej. usuario@dominio.com).</p>
            </div>
          </div>

          <!-- ============================== -->
          <!-- CAMPO LOGIN (USUARIO / CORREO) -->
          <!-- ============================== -->
          <template v-else class="animate-fade-in">
            <div>
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Nombre de Usuario</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                </div>
                <input 
                  v-model="form.username" 
                  type="text" 
                  required 
                  placeholder="Ej. jperez" 
                  class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-4 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
                >
              </div>
            </div>
          </template>
          
          <!-- ============================== -->
          <!-- CAMPO CONTRASEÑA (COMPARTIDO)  -->
          <!-- ============================== -->
          <div :class="{'md:col-span-2': esRegistro}" class="animate-fade-in">
            <div class="flex justify-between items-center mb-2">
              <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest">Contraseña</label>
              <a v-if="!esRegistro" href="#" class="text-[10px] font-bold text-medical-blue hover:underline">¿La olvidaste?</a>
            </div>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" /></svg>
              </div>
              <input 
                v-model="form.password" 
                :type="mostrarPassword ? 'text' : 'password'" 
                required 
                placeholder="••••••••" 
                class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-11 pr-12 focus:ring-2 focus:ring-medical-blue focus:bg-white outline-none transition-all font-medium text-slate-700"
              >
              <button 
                type="button" 
                @click="mostrarPassword = !mostrarPassword" 
                class="absolute inset-y-0 right-0 pr-4 flex items-center text-slate-400 hover:text-medical-blue transition-colors focus:outline-none"
              >
                <svg v-if="!mostrarPassword" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                <svg v-else class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" /></svg>
              </button>
            </div>

            <div v-if="esRegistro && form.password" class="mt-3">
              <div class="flex gap-1 h-1.5 w-full rounded-full overflow-hidden bg-slate-100">
                <div class="h-full transition-all duration-500" :class="colorFuerzaPassword" :style="`width: ${fuerzaPassword * 25}%`"></div>
              </div>
              <p class="text-[10px] font-bold mt-1 text-slate-400 text-right">{{ textoFuerzaPassword }}</p>
            </div>
          </div>

          <div v-if="mensajeError" class="p-4 bg-red-50 text-red-600 border border-red-200 rounded-xl text-sm font-bold flex items-start gap-3 shadow-sm animate-fade-in" :class="{'md:col-span-2': esRegistro}">
            <svg class="w-5 h-5 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>
            <span>{{ mensajeError }}</span>
          </div>
          
          <div v-if="mensajeExito" class="p-4 bg-emerald-50 text-emerald-600 border border-emerald-200 rounded-xl text-sm font-bold flex items-center gap-3 shadow-sm animate-fade-in" :class="{'md:col-span-2': esRegistro}">
            <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{{ mensajeExito }}</span>
          </div>

          <button 
            type="submit" 
            :disabled="procesando || !esFormularioValido" 
            class="w-full bg-slate-900 hover:bg-medical-blue text-white py-4 rounded-xl font-black text-lg tracking-wide transition-all shadow-lg hover:shadow-medical-blue/30 active:scale-95 disabled:opacity-50 disabled:active:scale-100 flex justify-center items-center gap-2 mt-6"
            :class="{'md:col-span-2': esRegistro}"
          >
            <span v-if="procesando" class="animate-spin w-5 h-5 border-2 border-white border-t-transparent rounded-full"></span>
            {{ procesando ? 'Procesando...' : (esRegistro ? 'Registrar mi cuenta' : 'Ingresar a mi cuenta') }}
          </button>
        </form>

        <div class="mt-8 pt-6 border-t border-slate-100 text-center">
          <p class="text-sm text-slate-500 mb-4">
            {{ esRegistro ? '¿Ya tienes una cuenta?' : '¿Eres nuevo en Ferretería Cruz?' }}
          </p>
          <button 
            @click="cambiarModo" 
            type="button" 
            class="text-medical-blue font-black hover:text-medical-dark transition-colors px-6 py-2 rounded-full border-2 border-medical-blue hover:bg-blue-50 w-full md:w-auto"
          >
            {{ esRegistro ? 'Iniciar Sesión' : 'Regístrate gratis' }}
          </button>
        </div>

      </div>

      <!-- Lado Derecho: Branding e Imagen -->
      <div class="md:w-1/2 bg-slate-900 relative hidden md:block group">
        <img 
          src="https://images.unsplash.com/photo-1586864387967-d02ef85d93e8?q=80&w=2070&auto=format&fit=crop" 
          class="absolute inset-0 w-full h-full object-cover opacity-50 group-hover:scale-105 transition-transform duration-1000" 
          alt="Herramientas Ferretería"
        />
        <div class="absolute inset-0 bg-gradient-to-t from-slate-900 via-slate-900/40 to-transparent"></div>
        <div class="absolute bottom-0 left-0 p-12 text-white">
          <div class="w-14 h-14 bg-medical-blue rounded-2xl flex items-center justify-center mb-6 shadow-xl shadow-medical-blue/30 backdrop-blur-md border border-white/10">
            <svg class="w-7 h-7 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 4a2 2 0 114 0v1a1 1 0 001 1h3a1 1 0 011 1v3a1 1 0 01-1 1h-1a2 2 0 100 4h1a1 1 0 011 1v3a1 1 0 01-1 1h-3a1 1 0 01-1-1v-1a2 2 0 10-4 0v1a1 1 0 01-1 1H7a1 1 0 01-1-1v-3a1 1 0 00-1-1H4a2 2 0 110-4h1a1 1 0 001-1V7a1 1 0 011-1h3a1 1 0 001-1V4z"></path></svg>
          </div>
          <h2 class="text-4xl font-black mb-4 leading-tight tracking-tight">Potencia tus<br>proyectos</h2>
          <p class="text-slate-300 text-sm leading-relaxed max-w-sm">
            Ferretería Cruz te brinda las mejores herramientas y materiales. Únete y obtén un control total sobre tus pedidos, facturas y seguimiento en tiempo real.
          </p>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { authStore } from '@/store/auth';
import { carritoStore } from '@/store/carrito';
import { normalizarSesionAutenticacion } from '@/utils/auth';

const router = useRouter();
const route = useRoute();

const esRegistro = ref(false);
const procesando = ref(false);
const mensajeError = ref('');
const mensajeExito = ref('');
const mostrarPassword = ref(false);

const form = reactive({ 
  username: '', 
  password: '',
  dni: '',
  nombre: '',
  apellido: '',
  correo: ''
});

const BASE_URL = (import.meta.env.VITE_API_URL || '/api') + '/v1';

const esCorreoValido = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(form.correo);
});

const fuerzaPassword = computed(() => {
  const p = form.password;
  let score = 0;
  if (p.length >= 6) score += 1;
  if (p.length >= 8) score += 1;
  if (/[A-Z]/.test(p)) score += 1;
  if (/[0-9]/.test(p) || /[^A-Za-z0-9]/.test(p)) score += 1;
  return score;
});

const colorFuerzaPassword = computed(() => {
  if (fuerzaPassword.value <= 1) return 'bg-red-400';
  if (fuerzaPassword.value === 2) return 'bg-amber-400';
  if (fuerzaPassword.value === 3) return 'bg-blue-400';
  return 'bg-emerald-500';
});

const textoFuerzaPassword = computed(() => {
  if (fuerzaPassword.value <= 1) return 'Débil';
  if (fuerzaPassword.value === 2) return 'Regular';
  if (fuerzaPassword.value === 3) return 'Buena';
  return 'Fuerte';
});

const esFormularioValido = computed(() => {
  if (esRegistro.value) {
    return esCorreoValido.value && form.dni.length >= 8 && form.nombre.length > 2 && form.apellido.length > 2 && form.username.length > 2 && fuerzaPassword.value >= 2;
  }
  return form.username.length > 0 && form.password.length > 0;
});

const limpiarErrores = () => {
  mensajeError.value = '';
  mensajeExito.value = '';
  form.password = '';
  if(esRegistro.value) {
      form.username = ''; 
  }
};

const procesarFormulario = async () => {
  procesando.value = true;
  mensajeError.value = '';
  mensajeExito.value = '';

  try {
    if (esRegistro.value) {
      const res = await fetch(`${BASE_URL}/usuarios/registro-tienda`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          username: form.username, 
          password: form.password,
          dni: form.dni,
          nombre: form.nombre,
          apellido: form.apellido,
          correo: form.correo
        })
      });

      if (!res.ok) {
        mensajeError.value = 'El nombre de usuario ya se encuentra registrado o hubo un error.';
        return;
      }

      mensajeExito.value = "¡Cuenta creada con éxito! Ahora inicia sesión.";
      setTimeout(() => {
        esRegistro.value = false;
        limpiarErrores();
        form.username = ''; 
      }, 2000);

    } else {
      const res = await fetch(`${BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: form.username, password: form.password })
      });

      if (!res.ok) {
        mensajeError.value = 'Usuario o contraseña incorrectos. Verifica tus datos.';
        return;
      }

      const data = await res.json();
      const usuarioAutenticado = normalizarSesionAutenticacion(data);

      if (usuarioAutenticado.rol !== 'CLIENTE') {
        mensajeError.value = 'Acceso denegado. Las cuentas de empleado deben usar el Panel Administrativo.';
        return;
      }

      authStore.iniciarSesion(usuarioAutenticado);
      await carritoStore.cargarCarritoBD(usuarioAutenticado.idUsuario || usuarioAutenticado.id);

      const redireccion = Array.isArray(route.query.redirect)
        ? route.query.redirect[0]
        : route.query.redirect || '/';
        
      router.push(redireccion);
    }
  } catch (error) {
    mensajeError.value = "Error de conexión con el servidor. Inténtalo más tarde.";
  } finally {
    procesando.value = false;
  }
};
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.3s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-blob {
  animation: blob 7s infinite;
}
.animation-delay-2000 {
  animation-delay: 2s;
}

@keyframes blob {
  0% { transform: translate(0px, 0px) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
  100% { transform: translate(0px, 0px) scale(1); }
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
  border-radius: 20px;
}
</style>