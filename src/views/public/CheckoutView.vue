<template>
  <div class="max-w-300 mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <h1 class="text-3xl font-black text-slate-800 mb-8">Finalizar Pedido</h1>
    
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
      
      <div class="lg:col-span-8 space-y-6">
        
        <!-- Banner Inteligente de Auto-completado -->
        <div v-if="datosCargados" class="p-4 bg-emerald-50 border border-emerald-200 rounded-2xl flex gap-4 items-center shadow-sm animate-fade-in">
            <div class="w-10 h-10 bg-emerald-500 text-white rounded-full flex items-center justify-center shrink-0 shadow-md">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
            </div>
            <div>
                <h3 class="font-black text-emerald-700 text-sm">¡Hemos cargado tus datos!</h3>
                <p class="text-xs text-emerald-600">Recuperamos tu información y dirección de tu última compra para ahorrarte tiempo.</p>
            </div>
        </div>

        <div class="bg-white p-6 md:p-8 rounded-4xl shadow-sm border border-slate-200">
          <h2 class="text-lg font-black text-slate-800 mb-6 flex items-center gap-3">
            <span class="w-8 h-8 rounded-full bg-medical-blue text-white flex items-center justify-center text-xs shadow-md">1</span>
            Datos de Contacto
          </h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Nombres</label>
              <input v-model="form.nombre" type="text" placeholder="Ej. Juan" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium capitalize" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Apellidos</label>
              <input v-model="form.apellido" type="text" placeholder="Ej. Pérez" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium capitalize" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">DNI / RUC</label>
              <input v-model="form.docCliente" type="text" maxlength="11" placeholder="Ej. 70123456" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Teléfono Móvil</label>
              <input v-model="form.telefono" type="tel" placeholder="Ej. 987654321" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Correo Electrónico</label>
              <input v-model="form.email" type="email" placeholder="correo@ejemplo.com" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
          </div>
        </div>

        <div class="bg-white p-6 md:p-8 rounded-4xl shadow-sm border border-slate-200">
          <h2 class="text-lg font-black text-slate-800 mb-6 flex items-center gap-3">
            <span class="w-8 h-8 rounded-full bg-medical-blue text-white flex items-center justify-center text-xs shadow-md">2</span>
            Dirección de Envío
          </h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="md:col-span-2">
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Dirección (Calle/Av.)</label>
              <input v-model="form.direccion" type="text" placeholder="Ej. Av. Los Pinos" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Número</label>
              <input v-model="form.numeroCalle" type="text" placeholder="Ej. 123" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Dpto / Piso (Opcional)</label>
              <input v-model="form.apartamento" type="text" placeholder="Ej. Dpto 301" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Departamento / Región</label>
              <input v-model="form.departamento" type="text" placeholder="Ej. Lima" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
            <div>
              <label class="block text-xs font-black text-slate-400 uppercase mb-2">Ciudad / Distrito</label>
              <input v-model="form.ciudad" type="text" placeholder="Ej. Miraflores" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 px-4 focus:ring-2 focus:ring-medical-blue outline-none transition-all font-medium" />
            </div>
          </div>
        </div>

        <div class="bg-white p-6 md:p-8 rounded-4xl shadow-sm border border-slate-200">
          <h2 class="text-lg font-black text-slate-800 mb-6 flex items-center gap-3">
            <span class="w-8 h-8 rounded-full bg-medical-blue text-white flex items-center justify-center text-xs shadow-md">3</span>
            Opciones de Envío
          </h2>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <button @click="form.tipoEnvio = 'NORMAL'" :class="form.tipoEnvio === 'NORMAL' ? 'border-medical-blue bg-blue-50 ring-2 ring-medical-blue/20' : 'border-slate-200'" class="p-4 border-2 rounded-2xl text-left transition-all">
              <span class="block font-black text-slate-800">Normal</span>
              <span class="text-xs text-slate-500 block mb-2">Hasta 5 días hábiles</span>
              <span class="text-sm font-bold text-medical-blue">S/. 10.00</span>
            </button>
            <button @click="form.tipoEnvio = 'EXPRESS'" :class="form.tipoEnvio === 'EXPRESS' ? 'border-medical-blue bg-blue-50 ring-2 ring-medical-blue/20' : 'border-slate-200'" class="p-4 border-2 rounded-2xl text-left transition-all">
              <span class="block font-black text-slate-800">Express</span>
              <span class="text-xs text-slate-500 block mb-2">Hasta 48 horas</span>
              <span class="text-sm font-bold text-medical-blue">S/. 15.00</span>
            </button>
            <button @click="form.tipoEnvio = 'SAME_DAY'" :class="form.tipoEnvio === 'SAME_DAY' ? 'border-medical-blue bg-blue-50 ring-2 ring-medical-blue/20' : 'border-slate-200'" class="p-4 border-2 rounded-2xl text-left transition-all">
              <span class="block font-black text-slate-800">Mismo Día</span>
              <span class="text-xs text-slate-500 block mb-2">Llega hoy mismo</span>
              <span class="text-sm font-bold text-medical-blue">S/. 25.00</span>
            </button>
          </div>
        </div>

        <div class="bg-white p-6 md:p-8 rounded-4xl shadow-sm border border-slate-200">
          <h2 class="text-lg font-black text-slate-800 mb-6 flex items-center gap-3">
            <span class="w-8 h-8 rounded-full bg-medical-blue text-white flex items-center justify-center text-xs shadow-md">4</span>
            Pago y Comprobante
          </h2>
          
          <div class="mb-6">
            <label class="block text-xs font-black text-slate-400 uppercase mb-3">Comprobante de Pago</label>
            <div class="flex gap-4">
              <button @click="form.tipoComprobante = 'BOLETA'" :class="form.tipoComprobante === 'BOLETA' ? 'bg-slate-800 text-white' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'" class="px-6 py-3 rounded-xl font-bold transition-colors">Boleta</button>
              <button @click="form.tipoComprobante = 'FACTURA'" :class="form.tipoComprobante === 'FACTURA' ? 'bg-slate-800 text-white' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'" class="px-6 py-3 rounded-xl font-bold transition-colors">Factura</button>
            </div>
          </div>

          <label class="block text-xs font-black text-slate-400 uppercase mb-3">Método de Pago</label>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <button v-for="metodo in metodosPago" :key="metodo.id" @click="form.metodoPago = metodo.id" :class="form.metodoPago === metodo.id ? 'border-medical-blue bg-blue-50 ring-2 ring-medical-blue/20' : 'border-slate-200'" class="p-4 border-2 rounded-2xl text-center transition-all hover:border-medical-blue">
              <span class="block text-sm font-black text-slate-800">{{ metodo.nombre }}</span>
            </button>
          </div>

          <!-- Componente Inteligente de Tarjeta de Crédito -->
          <div v-if="form.metodoPago.includes('TARJETA')" class="bg-slate-50 p-6 rounded-2xl border border-slate-200 overflow-hidden relative">
            
            <h3 class="text-sm font-black text-slate-700 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path></svg>
              Datos de la Tarjeta
            </h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 relative z-10">
              <div class="md:col-span-2">
                <input v-model="form.tarjeta.titular" type="text" placeholder="Nombre impreso en la tarjeta" class="w-full bg-white border border-slate-200 rounded-lg py-2.5 px-4 outline-none focus:border-medical-blue uppercase" />
              </div>
              <div class="md:col-span-2 relative">
                <!-- Formateador Inteligente de Tarjeta -->
                <input v-model="form.tarjeta.numero" @input="formatearTarjeta" type="text" placeholder="0000 0000 0000 0000" class="w-full bg-white border border-slate-200 rounded-lg py-2.5 px-4 outline-none focus:border-medical-blue font-mono tracking-widest text-lg" />
                
                <!-- Logo dinámico Visa/Mastercard -->
                <div class="absolute right-3 top-1/2 -translate-y-1/2">
                    <span v-if="marcaTarjeta === 'VISA'" class="text-blue-700 font-black italic text-xl">VISA</span>
                    <span v-else-if="marcaTarjeta === 'MASTERCARD'" class="text-red-500 font-black italic text-xl">MasterCard</span>
                </div>
              </div>
              <div>
                <input v-model="form.tarjeta.vencimiento" @input="formatearVencimiento" type="text" placeholder="MM/YY" class="w-full bg-white border border-slate-200 rounded-lg py-2.5 px-4 outline-none focus:border-medical-blue text-center font-mono text-lg" />
              </div>
              <div>
                <input v-model="form.tarjeta.cvv" @input="formatearCVV" type="password" placeholder="CVV" class="w-full bg-white border border-slate-200 rounded-lg py-2.5 px-4 outline-none focus:border-medical-blue text-center font-mono text-lg tracking-[0.3em]" />
              </div>
            </div>
          </div>

        </div>

      </div>

      <div class="lg:col-span-4">
          <div class="bg-slate-900 rounded-4xl p-6 md:p-8 text-white sticky top-28 shadow-xl">
          <h2 class="text-xl font-black mb-6 flex items-center gap-2">
            <svg class="w-5 h-5 text-medical-accent" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"></path></svg>
            Resumen del Pedido
          </h2>
          
          <div class="space-y-4 mb-6">
            <div class="flex justify-between text-slate-400 font-medium">
              <span>Subtotal Productos</span>
              <span class="text-white">S/. {{ format(carritoStore.subtotalPrecio / 1.18) }}</span>
            </div>
            <div class="flex justify-between text-slate-400 font-medium">
              <span>IGV (18%)</span>
              <span class="text-white">S/. {{ format(carritoStore.subtotalPrecio - (carritoStore.subtotalPrecio / 1.18)) }}</span>
            </div>
            <div class="flex justify-between text-slate-400 font-medium">
              <span>Costo de Envío</span>
              <span class="text-medical-accent font-bold">S/. {{ format(costoEnvioCalculado) }}</span>
            </div>
          </div>
          
          <div class="flex justify-between items-end pt-6 border-t border-slate-700 mb-8">
            <span class="text-lg text-slate-300">Total a Pagar</span>
            <span class="text-3xl font-black text-white">S/. {{ format(totalFinal) }}</span>
          </div>
          
          <button @click="procesarPago" :disabled="procesando" class="w-full bg-medical-accent hover:bg-green-500 py-4 rounded-xl font-black text-lg transition-all active:scale-95 shadow-lg shadow-medical-accent/20 disabled:opacity-50 disabled:active:scale-100 flex justify-center items-center gap-2">
            <span v-if="procesando" class="animate-spin w-5 h-5 border-2 border-white border-t-transparent rounded-full"></span>
            {{ procesando ? 'Procesando...' : 'Confirmar y Pagar' }}
          </button>
          
          <p class="text-xs text-slate-500 mt-6 text-center">
            Tus datos personales y de pago están protegidos por encriptación de extremo a extremo.
          </p>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { carritoStore } from '@/store/carrito';
import { authStore } from '@/store/auth';
import { apiClient } from '@/services/apiClient';

const router = useRouter();
const procesando = ref(false);
const datosCargados = ref(false);

const form = reactive({
  nombre: '',
  apellido: '',
  docCliente: '',
  email: '',
  telefono: '',
  direccion: '',
  numeroCalle: '',
  apartamento: '',
  ciudad: '',
  departamento: '',
  tipoEnvio: 'NORMAL', 
  tipoComprobante: 'BOLETA',
  metodoPago: 'TARJETA_CREDITO',
  tarjeta: {
    titular: '',
    numero: '',
    vencimiento: '',
    cvv: ''
  }
});

// ==========================================
// 💡 CEREBRO INTELIGENTE DE AUTO-LLENADO
// ==========================================
onMounted(async () => {
  if (authStore.estaLogueado) {
    const idUser = authStore.usuarioActual.idUsuario || authStore.usuarioActual.id;
    
    try {
      // 1. CARGAMOS DATOS PERSONALES DESDE LA NUEVA TABLA (usuarios_clientes)
      const perfilNuevo = await apiClient.obtenerPerfilCliente(idUser);
      
      if (perfilNuevo) {
        form.nombre = perfilNuevo.nombreCompleto || '';
        form.apellido = perfilNuevo.apellido || '';
        form.docCliente = perfilNuevo.dni || '';
        form.email = perfilNuevo.correo || '';
      }

      // 2. BUSCAMOS HISTORIAL DE COMPRAS (venta_cliente)
      const historial = await apiClient.obtenerMisCompras(idUser);
      
      if (historial && historial.length > 0) {
        const ultimaCompra = historial[0]; 
        
        // Solo rellenamos lo que falta (Dirección y teléfono)
        form.telefono = ultimaCompra.telefonoCliente || '';
        form.direccion = ultimaCompra.direccionEnvio || '';
        form.numeroCalle = ultimaCompra.numeroCalle || '';
        form.apartamento = ultimaCompra.apartamento || '';
        form.ciudad = ultimaCompra.ciudad || '';
        form.departamento = ultimaCompra.departamento || '';
        
        // Activamos el banner solo si recuperamos historial logístico
        datosCargados.value = true;
      }
    } catch (error) {
      console.warn("No se pudo auto-llenar los datos.");
    }
  }
});

// ==========================================
// 💳 FORMATEADORES DE TARJETA EN VIVO
// ==========================================
const marcaTarjeta = computed(() => {
    if (form.tarjeta.numero.startsWith('4')) return 'VISA';
    if (form.tarjeta.numero.startsWith('5')) return 'MASTERCARD';
    return null;
});

const formatearTarjeta = () => {
  let valor = form.tarjeta.numero.replace(/\D/g, '');
  let formateado = '';
  for (let i = 0; i < valor.length; i++) {
    if (i > 0 && i % 4 === 0) formateado += ' ';
    formateado += valor[i];
  }
  form.tarjeta.numero = formateado.substring(0, 19); 
};

const formatearVencimiento = () => {
  let valor = form.tarjeta.vencimiento.replace(/\D/g, '');
  if (valor.length > 2) {
    valor = valor.substring(0, 2) + '/' + valor.substring(2, 4);
  }
  form.tarjeta.vencimiento = valor.substring(0, 5);
};

const formatearCVV = () => {
  form.tarjeta.cvv = form.tarjeta.cvv.replace(/\D/g, '').substring(0, 4);
};

const metodosPago = [
  { id: 'TARJETA_CREDITO', nombre: 'T. Crédito' },
  { id: 'TARJETA_DEBITO', nombre: 'T. Débito' },
  { id: 'YAPE', nombre: 'Yape / Plin' },
  { id: 'TRANSFERENCIA', nombre: 'Transferencia' }
];

const format = (n) => Number(n).toFixed(2);

const costoEnvioCalculado = computed(() => {
  if (form.tipoEnvio === 'NORMAL') return 10.00;
  if (form.tipoEnvio === 'EXPRESS') return 15.00;
  if (form.tipoEnvio === 'SAME_DAY') return 25.00;
  return 0;
});

const totalFinal = computed(() => {
  return carritoStore.subtotalPrecio + costoEnvioCalculado.value;
});

const procesarPago = async () => {
  if (!form.nombre || !form.apellido || !form.docCliente || !form.direccion || !form.ciudad) {
    alert("Por favor completa todos tus datos personales y de envío antes de continuar.");
    return;
  }

  const usuarioGuardado = authStore.usuarioActual;
  if (!usuarioGuardado || (!usuarioGuardado.idUsuario && !usuarioGuardado.id)) {
    alert("Tu sesión ha expirado. Inicia sesión nuevamente.");
    router.push('/login?redirect=/checkout');
    return;
  }

  procesando.value = true;
  try {
    const itemsCarrito = carritoStore.items.map(item => {
      return {
        idProducto: item.producto.idProducto,
        cantidad: item.cantidad,
        precioUnitario: item.producto.precio
      };
    });

    let ultimos4 = null;
    let marca = null;

    if (form.metodoPago.includes('TARJETA') && form.tarjeta.numero) {
      const numLimpio = form.tarjeta.numero.replace(/\s+/g, '');
      ultimos4 = numLimpio.slice(-4);
      marca = marcaTarjeta.value;
    }

    const payloadMaestro = {
      idUsuario: usuarioGuardado.idUsuario || usuarioGuardado.id,
      dniCliente: form.docCliente,
      nombreCliente: form.nombre,
      apellidoCliente: form.apellido,
      emailCliente: form.email,
      telefonoCliente: form.telefono,
      
      direccionEnvio: form.direccion,
      numeroCalle: form.numeroCalle,
      apartamento: form.apartamento,
      ciudad: form.ciudad,
      departamento: form.departamento,
      codigoPostal: "LIMA-01",
      
      tipoEnvio: form.tipoEnvio,
      costoEnvio: costoEnvioCalculado.value,
      
      tipoPago: form.metodoPago,
      tipoTarjeta: marca,
      ultimos4Digitos: ultimos4,
      bancoTarjeta: form.metodoPago.includes('TARJETA') ? "Web E-commerce" : null,
      nombreTitular: form.tarjeta.titular,
      
      items: itemsCarrito, 
      
      observaciones: `Comprobante solicitado: ${form.tipoComprobante}`
    };

    const respuestaBackend = await apiClient.crearPedido(payloadMaestro);

    await carritoStore.vaciarBD(usuarioGuardado.idUsuario || usuarioGuardado.id);

    const ticketFinal = respuestaBackend.pedido.nroPedido; 
    
    router.push({ 
      path: '/confirmacion', 
      query: { ticket: ticketFinal } 
    });

  } catch (err) {
    console.error(err);
    if (String(err.message || '').toLowerCase().includes('sesión')) {
      authStore.cerrarSesion();
      router.push('/login?redirect=/checkout');
      return;
    }
    alert("Error procesando pedido: " + (err.message || "Intenta de nuevo"));
  } finally {
    procesando.value = false;
  }
};
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.4s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>