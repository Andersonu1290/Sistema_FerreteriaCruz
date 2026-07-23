<template>
  <div>
    <div class="dashboard-container">
      
      <div class="header-tech">
        <div class="header-title">
          <h2 class="text-white">
            <svg viewBox="0 0 24 24" fill="none" stroke="var(--brand-blue)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14 2 14 8 20 8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10 9 9 9 8 9"></polyline>
            </svg>
            Auditoría e Historial de Ventas
          </h2>
        </div>
        <a href="/admin/venta" class="btn-tech">Volver al Punto de Venta</a>
      </div>

      <div style="display: flex; gap: 15px; margin-bottom: 25px; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 15px;">
        <button @click="tabActual = 'WEB'" class="btn-tech" :style="tabActual === 'WEB' ? 'background: var(--brand-blue); color: #fff;' : 'background: transparent; opacity: 0.6;'">
            Pedidos Web (E-commerce)
        </button>
        <button @click="tabActual = 'POS'" class="btn-tech" :style="tabActual === 'POS' ? 'background: var(--brand-blue); color: #fff;' : 'background: transparent; opacity: 0.6;'">
            Ventas Físicas (Local)
        </button>
      </div>

      <!-- ========================================== -->
      <!-- TAB 1: PEDIDOS WEB                         -->
      <!-- ========================================== -->
      <div v-if="tabActual === 'WEB'">
        <div class="table-panel">
          <table class="tech-table">
            <thead>
              <tr>
                <th>FECHA / HORA</th>
                <th>N° COMPROBANTE</th>
                <th>DATOS DEL CLIENTE</th>
                <th>PRODUCTOS (WEB)</th>
                <th>TOTAL</th>
                <th class="text-center">ESTADO</th>
                <th class="text-center">ACCIÓN</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="pedidosWeb.length === 0">
                <td colspan="7" class="text-center p-30 text-muted">Cargando pedidos web...</td>
              </tr>
              <tr v-for="pedido in pedidosWeb" :key="pedido.idVentaCliente">
                <td>
                  <div class="font-bold">{{ formatearFecha(pedido.fechaPedido) }}</div>
                </td>
                <td>
                  <div class="font-black text-blue-600 dark:text-blue-400 text-base">{{ pedido.nroPedido }}</div>
                  <div class="text-[11px] font-bold uppercase mt-1 text-slate-500 dark:text-slate-400">MÉTODO: {{ pedido.tipoPago.replace('_', ' ') }}</div>
                </td>
                <td>
                  <div class="font-bold">{{ pedido.nombreCliente }}</div>
                  <div class="text-xs mt-1 text-slate-600 dark:text-slate-400">🆔 DNI/RUC: {{ pedido.dniCliente || 'Sin DNI' }}</div>
                  <div class="text-xs mt-0.5 text-slate-600 dark:text-slate-400">📞 {{ pedido.telefonoCliente }} | ✉️ {{ pedido.emailCliente }}</div>
                  <div class="text-xs mt-0.5 text-slate-600 dark:text-slate-400">📍 {{ pedido.direccionEnvio }} {{ pedido.numeroCalle || '' }}</div>
                </td>
                <td>
                  <div class="text-sm mb-2 text-slate-700 dark:text-slate-300">
                    <div v-for="item in pedido.detalles" :key="item.idDetalle" class="mb-1.5 flex items-center gap-2">
                      <span class="inline-flex items-center justify-center w-5 h-5 rounded-full bg-blue-100 dark:bg-blue-900/50 text-blue-700 dark:text-blue-300 text-[10px] font-black shrink-0">
                          {{ item.cantidad }}
                      </span>
                      <span class="font-medium">{{ item.nombreProducto }}</span>
                    </div>
                  </div>
                  <div class="text-[10px] text-blue-600 dark:text-blue-400 font-bold uppercase tracking-widest">🚚 ENVÍO: {{ pedido.tipoEnvio }}</div>
                </td>
                <td>
                  <div class="font-black text-lg whitespace-nowrap">S/. {{ Number(pedido.total).toFixed(2) }}</div>
                </td>
                <td class="text-center">
                  <span v-if="pedido.estado === 'PENDIENTE'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-amber-100 text-amber-700 border border-amber-200">
                    <span class="w-2 h-2 rounded-full bg-amber-500 animate-pulse"></span> {{ pedido.estado }}
                  </span>
                  <span v-else-if="pedido.estado === 'ENVIADO'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-blue-100 text-blue-700 border border-blue-200">
                    <span class="w-2 h-2 rounded-full bg-blue-500"></span> {{ pedido.estado }}
                  </span>
                  <span v-else-if="pedido.estado === 'ENTREGADO'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-emerald-100 text-emerald-700 border border-emerald-200">
                    <span class="w-2 h-2 rounded-full bg-emerald-500"></span> {{ pedido.estado }}
                  </span>
                  <span v-else-if="pedido.estado === 'CANCELADO'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-red-100 text-red-700 border border-red-200">
                    <span class="w-2 h-2 rounded-full bg-red-500"></span> {{ pedido.estado }}
                  </span>
                </td>
                <td class="text-center">
                  <div class="flex flex-col gap-2 items-center">
                    
                    <!-- NUEVO BOTÓN: VER BOLETA WEB -->
                    <button @click="abrirBoleta(pedido, 'WEB')" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-slate-100 border border-slate-300 text-slate-700 hover:bg-slate-200 transition-colors w-32 flex justify-center items-center gap-2">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
                      Ver Boleta
                    </button>

                    <button v-if="pedido.estado === 'PENDIENTE'" @click="abrirModal(pedido, 'ENVIAR')" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-blue-50 border-2 border-blue-500 text-blue-600 hover:bg-blue-100 transition-colors w-32">
                        Marcar Enviado
                    </button>
                    <button v-if="pedido.estado === 'ENVIADO'" @click="abrirModal(pedido, 'ENTREGAR')" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-emerald-50 border-2 border-emerald-500 text-emerald-600 hover:bg-emerald-100 transition-colors w-32">
                        Entregado
                    </button>
                    <button v-if="pedido.estado === 'PENDIENTE'" @click="abrirModal(pedido, 'REVERTIR')" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-red-50 border-2 border-red-500 text-red-600 hover:bg-red-100 transition-colors w-32">
                        Revertir Venta
                    </button>
                    <div v-if="pedido.estado === 'ENTREGADO' || pedido.estado === 'CANCELADO'" class="px-3 py-1 rounded-lg bg-slate-100 text-slate-500 text-[10px] font-black uppercase tracking-widest w-32 text-center border border-slate-200">
                        Logística Fin
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- TAB 2: VENTAS POS (MIGRADO 100% A VUE)     -->
      <!-- ========================================== -->
      <div v-show="tabActual === 'POS'">
        <div class="table-panel">
          <table class="tech-table">
            <thead>
              <tr>
                <th>FECHA / HORA</th>
                <th>N° COMPROBANTE</th>
                <th>CLIENTE</th>
                <th>PRODUCTO FÍSICO (S/N)</th>
                <th>TOTAL</th>
                <th class="text-center">ESTADO</th>
                <th class="text-center">ACCIÓN</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="ventasPOS.length === 0">
                <td colspan="7" class="text-center p-30 text-muted">Cargando auditoría de transacciones...</td>
              </tr>
              <tr v-for="venta in ventasPOS" :key="venta.id">
                <td>
                  <div class="font-bold">{{ formatearFecha(venta.fecha) }}</div>
                </td>
                <td>
                  <div class="font-black text-blue-600 dark:text-blue-400 text-base">{{ venta.nroComprobante }}</div>
                  <div class="text-[11px] font-bold uppercase mt-1 text-slate-500">MÉTODO: {{ venta.metodoPago }}</div>
                </td>
                <td>
                  <div class="font-bold">{{ venta.nombreCliente }}</div>
                  <div class="text-xs mt-1 text-slate-600 dark:text-slate-400 font-medium">🆔 DNI/RUC: {{ venta.dniCliente || 'Sin DNI' }}</div>
                </td>
                <td>
                  <div class="text-sm font-medium mb-1">{{ venta.nombreProducto }}</div>
                  <div class="text-[10px] text-slate-500 font-bold uppercase tracking-widest">S/N: {{ venta.nroSerie }}</div>
                </td>
                <td>
                  <div class="font-black text-lg whitespace-nowrap">S/. {{ Number(venta.total).toFixed(2) }}</div>
                </td>
                <td class="text-center">
                  <span v-if="venta.estado === 'COMPLETADA'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-emerald-100 text-emerald-700 border border-emerald-200">
                    <span class="w-2 h-2 rounded-full bg-emerald-500"></span> COMPLETADA
                  </span>
                  <span v-else-if="venta.estado === 'ANULADA'" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[11px] font-black uppercase tracking-widest bg-red-100 text-red-700 border border-red-200">
                    <span class="w-2 h-2 rounded-full bg-red-500"></span> ANULADA
                  </span>
                </td>
                <td class="text-center">
                  <div class="flex flex-col gap-2 items-center">
                    
                    <!-- NUEVO BOTÓN: VER BOLETA POS -->
                    <button @click="abrirBoleta(venta, 'POS')" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-slate-100 border border-slate-300 text-slate-700 hover:bg-slate-200 transition-colors w-32 flex justify-center items-center gap-2">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
                      Ver Ticket
                    </button>

                    <button v-if="venta.estado === 'COMPLETADA'" @click="abrirModalAnularPOS(venta)" class="px-3 py-1.5 text-xs font-bold rounded-lg bg-red-50 border-2 border-red-500 text-red-600 hover:bg-red-100 transition-colors w-32">
                        Anular Venta
                    </button>
                    <div v-if="venta.estado === 'ANULADA'" class="px-3 py-1 rounded-lg bg-slate-100 text-slate-500 text-[10px] font-black uppercase tracking-widest w-32 text-center border border-slate-200">
                        Venta Anulada
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </div>

    <!-- ========================================== -->
    <!-- MODAL LOGÍSTICO (Pedidos Web)              -->
    <!-- ========================================== -->
    <div v-if="modal.visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4 print-hide">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden" style="background-color: #ffffff !important;">
        <div class="p-6 border-b border-slate-100 bg-slate-50" style="background-color: #f8fafc !important;">
          <h3 class="text-xl font-black text-slate-800" style="color: #0f172a !important;">{{ modal.titulo }}</h3>
          <p class="text-sm text-slate-500 mt-1" style="color: #64748b !important;">Pedido: <strong class="text-blue-600" style="color: #2563eb !important;">{{ modal.pedido.nroPedido }}</strong></p>
        </div>
        <div class="p-6">
          <div v-if="modal.tipo === 'ENVIAR'">
            <label class="block text-xs font-black text-slate-400 uppercase mb-2" style="color: #475569 !important;">Empresa y Nro. de Seguimiento</label>
            <input v-model="modal.tracking" type="text" placeholder="Ej: Olva Courier - TRK-98765" class="w-full bg-white border-2 border-slate-200 rounded-xl py-3 px-4 outline-none transition-all font-medium text-slate-800" style="background-color: #ffffff !important; color: #0f172a !important;">
            <p class="text-xs text-slate-400 mt-2" style="color: #94a3b8 !important;">El cliente verá este código en su panel de seguimiento.</p>
          </div>
          <div v-if="modal.tipo === 'ENTREGAR'">
            <p class="text-slate-700 font-medium text-sm" style="color: #334155 !important;">Estás a punto de confirmar que el cliente recibió su paquete satisfactoriamente.</p>
          </div>
          <div v-if="modal.tipo === 'REVERTIR'">
            <p class="text-red-600 font-medium text-sm" style="color: #dc2626 !important;">Estás a punto de anular este pedido web. Los productos regresarán automáticamente al almacén.</p>
          </div>
        </div>
        <div class="p-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3" style="background-color: #f8fafc !important;">
          <button @click="cerrarModal" :disabled="procesando" class="px-5 py-2.5 text-sm font-bold text-slate-600 hover:bg-slate-200 rounded-xl transition-colors" style="color: #475569 !important;">Cancelar</button>
          <button @click="ejecutarAccion" :disabled="procesando" class="px-5 py-2.5 text-sm font-bold text-white rounded-xl transition-all shadow-md active:scale-95" :class="modal.colorBtn" style="color: #ffffff !important;">
            {{ procesando ? 'Procesando...' : modal.textoBtn }}
          </button>
        </div>
      </div>
    </div>

    <!-- ========================================== -->
    <!-- MODAL ANULAR POS                           -->
    <!-- ========================================== -->
    <div v-if="modalAnular.visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4 print-hide">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden" style="background-color: #ffffff !important;">
        <div class="p-6 border-b border-red-100 bg-red-50 text-red-600" style="background-color: #fef2f2 !important;">
          <h3 class="text-xl font-black" style="color: #dc2626 !important;">Anular Venta Física</h3>
          <p class="text-sm mt-1" style="color: #dc2626 !important;">Ticket: <strong>{{ modalAnular.venta.nroComprobante }}</strong></p>
        </div>
        <div class="p-6">
          <p class="text-slate-700 font-medium text-sm" style="color: #334155 !important;">¿Estás seguro de anular esta venta? El número de serie <strong style="color: #0f172a !important;">{{ modalAnular.venta.nroSerie }}</strong> regresará a estado DISPONIBLE en el inventario.</p>
        </div>
        <div class="p-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3" style="background-color: #f8fafc !important;">
          <button @click="modalAnular.visible = false" :disabled="procesando" class="px-5 py-2.5 text-sm font-bold text-slate-600 hover:bg-slate-200 rounded-xl transition-colors" style="color: #475569 !important;">Cancelar</button>
          <button @click="procesarAnulacionPOS" :disabled="procesando" class="px-5 py-2.5 text-sm font-bold text-white bg-red-500 hover:bg-red-600 rounded-xl transition-all shadow-md active:scale-95" style="color: #ffffff !important;">
            {{ procesando ? 'Anulando...' : 'Sí, Anular Venta' }}
          </button>
        </div>
      </div>
    </div>

    <!-- ========================================== -->
    <!-- MODAL VISUALIZADOR DE BOLETA / TICKET      -->
    <!-- ========================================== -->
    <div v-if="modalBoleta.visible" class="fixed inset-0 z-[100] flex items-center justify-center bg-black/80 backdrop-blur-sm p-4 modal-boleta-container">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden print:w-full print:shadow-none" id="seccion-imprimir">
        
        <!-- Controles superiores (se ocultan al imprimir) -->
        <div class="flex justify-between items-center p-4 border-b border-slate-100 bg-slate-50 print-hide">
           <h3 class="font-black text-slate-700" style="color: #334155 !important;">Comprobante de Venta</h3>
           <button @click="cerrarBoleta" class="text-slate-400 hover:text-red-500 transition-colors">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
           </button>
        </div>

        <!-- Contenido del Ticket -->
        <div class="p-8 font-mono text-sm text-slate-800 bg-white">
           <div class="text-center mb-6">
              <h2 class="text-2xl font-black uppercase tracking-widest" style="color: #0f172a !important;">FERRETERÍA CRUZ</h2>
              <p class="text-xs mt-1 font-bold text-slate-500">R.U.C. 20546987123</p>
              <p class="text-[11px] text-slate-500">Av. Garcilaso de la Vega 1348, Lima</p>
              
              <div class="border-b-2 border-dashed border-slate-300 my-4"></div>
              
              <p class="font-black text-lg">{{ modalBoleta.tipo === 'WEB' ? 'COMPROBANTE ELECTRÓNICO' : 'TICKET DE VENTA (POS)' }}</p>
              <p class="font-bold text-slate-600">{{ modalBoleta.tipo === 'WEB' ? modalBoleta.datos.nroPedido : modalBoleta.datos.nroComprobante }}</p>
           </div>

           <div class="mb-5 text-xs space-y-1.5">
              <p><span class="font-black text-slate-500">FECHA:</span> <span class="font-bold">{{ formatearFecha(modalBoleta.tipo === 'WEB' ? modalBoleta.datos.fechaPedido : modalBoleta.datos.fecha) }}</span></p>
              <p><span class="font-black text-slate-500">CLIENTE:</span> <span class="font-bold uppercase">{{ modalBoleta.datos.nombreCliente }}</span></p>
              <p><span class="font-black text-slate-500">DNI/RUC:</span> <span class="font-bold">{{ modalBoleta.datos.dniCliente || '------' }}</span></p>
              <p><span class="font-black text-slate-500">MÉTODO PAGO:</span> <span class="font-bold">{{ modalBoleta.tipo === 'WEB' ? modalBoleta.datos.tipoPago.replace('_', ' ') : modalBoleta.datos.metodoPago }}</span></p>
           </div>

           <div class="border-b-2 border-dashed border-slate-300 my-4"></div>

           <!-- Encabezados de Tabla -->
           <div class="flex justify-between font-black text-xs mb-3 text-slate-500">
              <span class="w-10">CANT</span>
              <span class="flex-1">DESCRIPCIÓN</span>
              <span class="w-20 text-right">IMPORTE</span>
           </div>

           <!-- Items de Venta WEB -->
           <template v-if="modalBoleta.tipo === 'WEB'">
              <div v-for="item in modalBoleta.datos.detalles" :key="item.idDetalle" class="flex justify-between text-[11px] mb-3 font-bold">
                 <span class="w-10 text-center">{{ item.cantidad }}</span>
                 <span class="flex-1 pr-2 uppercase">{{ item.nombreProducto }}</span>
                 <span class="w-20 text-right">S/ {{ Number(item.subtotal).toFixed(2) }}</span>
              </div>
              <!-- Flete si existe -->
              <div v-if="modalBoleta.datos.costoEnvio > 0" class="flex justify-between text-[11px] mb-3 font-bold">
                 <span class="w-10 text-center">1</span>
                 <span class="flex-1 pr-2 uppercase">SERVICIO DE ENVÍO ({{ modalBoleta.datos.tipoEnvio }})</span>
                 <span class="w-20 text-right">S/ {{ Number(modalBoleta.datos.costoEnvio).toFixed(2) }}</span>
              </div>
           </template>

           <!-- Items de Venta POS -->
           <template v-else>
              <div class="flex justify-between text-[11px] mb-3 font-bold">
                 <span class="w-10 text-center">1</span>
                 <span class="flex-1 pr-2 uppercase">
                    {{ modalBoleta.datos.nombreProducto }}
                    <br>
                    <span class="text-[10px] text-slate-400 font-normal">S/N: {{ modalBoleta.datos.nroSerie }}</span>
                 </span>
                 <span class="w-20 text-right">S/ {{ Number(modalBoleta.datos.total).toFixed(2) }}</span>
              </div>
           </template>

           <div class="border-b-2 border-dashed border-slate-300 my-4"></div>

           <!-- Totales -->
           <div class="flex justify-between font-black text-lg">
              <span>TOTAL:</span>
              <span>S/ {{ Number(modalBoleta.tipo === 'WEB' ? modalBoleta.datos.total : modalBoleta.datos.total).toFixed(2) }}</span>
           </div>

           <div class="mt-10 text-center text-[10px] text-slate-400 font-bold uppercase tracking-widest">
              <p>Gracias por su preferencia</p>
              <p>www.ferreteriacruz.com</p>
           </div>
        </div>

        <!-- Botón de Imprimir -->
        <div class="p-4 bg-slate-50 border-t border-slate-100 flex justify-center print-hide">
           <button @click="imprimirBoleta" class="w-full hover:bg-medical-blue px-6 py-4 rounded-xl font-black text-lg transition-all shadow-xl flex items-center justify-center gap-3" style="background-color: #0f172a !important; color: #ffffff !important;">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" style="color: #ffffff !important;"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path></svg>
              Imprimir Comprobante
           </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { apiClient } from '@/services/apiClient';

export default {
  name: 'HistorialVentasView',
  data() {
    return {
      tabActual: 'WEB', 
      pedidosWeb: [],
      ventasPOS: [],
      procesando: false,
      
      modal: {
        visible: false,
        tipo: '',
        pedido: null,
        titulo: '',
        tracking: '',
        colorBtn: '',
        textoBtn: ''
      },

      modalAnular: {
        visible: false,
        venta: null
      },

      modalBoleta: {
        visible: false,
        datos: null,
        tipo: ''
      }
    };
  },
  mounted() {
    window.VITE_API_URL = import.meta.env.VITE_API_URL;
    
    // Solo cargamos utils, YA NO cargamos historialventas.js porque ahora Vue hace todo
    this.cargarScript('/admin/js/api.js')
      .then(() => this.cargarScript('/admin/js/utils.js'))
      .catch(err => console.error("Error al inyectar infraestructura:", err));
      
    // Carga de datos
    this.cargarPedidosWeb();
    this.cargarVentasPOS();
  },
  methods: {
    async cargarPedidosWeb() {
      try {
        this.pedidosWeb = await apiClient.obtenerTodosLosPedidosWeb();
      } catch (error) {
        console.error("Error al cargar pedidos web:", error);
      }
    },
    
    // NUEVO: Método para cargar las ventas locales
    async cargarVentasPOS() {
      try {
        this.ventasPOS = await apiClient.obtenerHistorialPOS();
      } catch (error) {
        console.error("Error al cargar ventas de caja:", error);
      }
    },

    // ===================================
    // LÓGICA DE BOLETA / TICKET
    // ===================================
    abrirBoleta(registro, tipo) {
      this.modalBoleta.datos = registro;
      this.modalBoleta.tipo = tipo;
      this.modalBoleta.visible = true;
    },
    
    cerrarBoleta() {
      this.modalBoleta.visible = false;
      this.modalBoleta.datos = null;
    },

    imprimirBoleta() {
      window.print();
    },

    // ===================================
    // LÓGICA DE ANULACIÓN POS EN VUE
    // ===================================
    abrirModalAnularPOS(venta) {
      this.modalAnular.venta = venta;
      this.modalAnular.visible = true;
    },

    async procesarAnulacionPOS() {
      this.procesando = true;
      try {
        const sesionString = sessionStorage.getItem('usuarioActivo');
        const adminToken = sessionStorage.getItem('jwt_token') || (sesionString ? JSON.parse(sesionString).token : '');
        const idUser = sesionString ? JSON.parse(sesionString).idUsuario : 0;
        
        const res = await fetch(`${window.VITE_API_URL}/ventas/anular/${this.modalAnular.venta.id}?idUsuario=${idUser}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${adminToken}`,
                'Content-Type': 'application/json'
            }
        });
        
        if (!res.ok) {
          const err = await res.json();
          throw new Error(err.error || "No se pudo anular la venta");
        }
        
        alert("Venta física anulada correctamente. El Stock regresó al almacén.");
        this.modalAnular.visible = false;
        await this.cargarVentasPOS(); // Recargar tabla
      } catch(e) {
        alert(e.message);
      } finally {
        this.procesando = false;
      }
    },

    // ===================================
    // LÓGICA LOGÍSTICA WEB
    // ===================================
    abrirModal(pedido, tipoAccion) {
      this.modal.pedido = pedido;
      this.modal.tipo = tipoAccion;
      this.modal.tracking = ''; 
      this.modal.visible = true;
      if (tipoAccion === 'ENVIAR') {
        this.modal.titulo = 'Registrar Despacho';
        this.modal.colorBtn = 'bg-blue-500 hover:bg-blue-600';
        this.modal.textoBtn = 'Confirmar Envío';
      } else if (tipoAccion === 'ENTREGAR') {
        this.modal.titulo = 'Confirmar Entrega';
        this.modal.colorBtn = 'bg-emerald-500 hover:bg-emerald-600';
        this.modal.textoBtn = 'Finalizar Pedido';
      } else if (tipoAccion === 'REVERTIR') {
        this.modal.titulo = 'Anular Pedido Web';
        this.modal.colorBtn = 'bg-red-500 hover:bg-red-600';
        this.modal.textoBtn = 'Revertir y Devolver Stock';
      }
    },

    cerrarModal() {
      this.modal.visible = false;
    },

    async ejecutarAccion() {
      this.procesando = true;
      try {
        if (this.modal.tipo === 'ENVIAR') {
          if (!this.modal.tracking) {
            alert("Por favor ingrese la empresa y número de seguimiento.");
            this.procesando = false;
            return;
          }
          await apiClient.actualizarEstadoPedidoWeb(this.modal.pedido.idVentaCliente, 'ENVIADO', this.modal.tracking);
        } else if (this.modal.tipo === 'ENTREGAR') {
          await apiClient.actualizarEstadoPedidoWeb(this.modal.pedido.idVentaCliente, 'ENTREGADO');
        } else if (this.modal.tipo === 'REVERTIR') {
          await apiClient.cancelarPedidoWeb(this.modal.pedido.idVentaCliente);
        }
        await this.cargarPedidosWeb();
        this.cerrarModal();
      } catch (error) {
        alert(error.message || "Hubo un error al procesar la acción.");
      } finally {
        this.procesando = false;
      }
    },

    formatearFecha(fechaStr) {
      if (!fechaStr) return '-';
      if (Array.isArray(fechaStr)) {
        const [year, month, day, hour = 0, minute = 0] = fechaStr;
        const fechaObj = new Date(year, month - 1, day, hour, minute);
        return fechaObj.toLocaleDateString('es-PE', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
      }
      return new Date(fechaStr).toLocaleDateString('es-PE', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
    },

    cargarScript(ruta) {
      return new Promise((resolve, reject) => {
        const scriptExistente = document.querySelector(`script[src="${ruta}"]`);
        if (scriptExistente) { resolve(); return; }
        const script = document.createElement('script');
        script.src = ruta;
        script.className = 'script-historial-modulo';
        script.async = true;
        script.onload = resolve;
        script.onerror = reject;
        document.body.appendChild(script);
      });
    }
  }
}
</script>

<style scoped>
/* ESTILOS PARA IMPRESIÓN DEL TICKET */
@media print {
  /* Ocultamos todo el panel de administración de fondo */
  body * {
    visibility: hidden;
  }
  
  /* Hacemos visible solo el contenedor del modal de la boleta */
  .modal-boleta-container {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    background: white !important;
  }
  
  .modal-boleta-container * {
    visibility: visible;
  }
  
  /* Ocultamos los botones de impresión y cerrar dentro del modal */
  .print-hide {
    display: none !important;
  }
}
</style>