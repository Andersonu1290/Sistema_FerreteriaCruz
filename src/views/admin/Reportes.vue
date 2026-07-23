<template>
  <div class="bg-slate-50 dark:bg-slate-900 min-h-screen pb-12 transition-colors duration-300">
    <div class="max-w-[100rem] mx-auto px-4 sm:px-6 lg:px-8 py-8 relative">

      <!-- PANTALLA DE CARGA FLOTANTE -->
      <div v-if="cargando" class="absolute inset-0 z-[100] flex flex-col items-center justify-start pt-64 bg-slate-50/80 dark:bg-slate-900/80 backdrop-blur-md rounded-3xl transition-all">
        <span class="animate-spin inline-block w-16 h-16 border-[6px] border-blue-600 dark:border-blue-500 border-t-transparent rounded-full mb-6 shadow-md"></span>
        <h3 class="text-2xl font-black text-slate-800 dark:text-white">Recalculando Métricas...</h3>
        <p class="text-slate-500 dark:text-slate-400 font-bold mt-2">Consultando la base de datos para: <span class="text-blue-600 dark:text-blue-400 uppercase tracking-widest">{{ textoFiltro }}</span></p>
      </div>
      
      <!-- ========================================== -->
      <!-- CABECERA PRINCIPAL                         -->
      <!-- ========================================== -->
      <div class="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-6 mb-8">
        <div class="flex items-center gap-5">
          <div class="w-16 h-16 rounded-2xl flex items-center justify-center shadow-lg shadow-blue-500/20 bg-gradient-to-br from-blue-600 to-indigo-600 dark:from-blue-500 dark:to-indigo-500">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="32" height="32" style="color: #ffffff !important;">
              <line x1="18" y1="20" x2="18" y2="10"></line>
              <line x1="12" y1="20" x2="12" y2="4"></line>
              <line x1="6" y1="20" x2="6" y2="14"></line>
            </svg>
          </div>
          <div>
            <h2 class="text-3xl font-black m-0 tracking-tight text-slate-900 dark:text-white transition-colors duration-300">Centro de Inteligencia</h2>
            <p class="text-sm font-medium mt-1 text-slate-500 dark:text-slate-400 transition-colors duration-300">Dashboard analítico de rendimiento y operaciones en tiempo real</p>
          </div>
        </div>

        <div class="flex flex-wrap items-center gap-3">
          <!-- FILTRO DE TIEMPO 100% FUNCIONAL Y REACTIVO -->
          <div class="hidden md:flex bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-xl p-1.5 shadow-sm mr-2 transition-colors duration-300">
            <button @click="filtroTiempo = 'mes'" :class="filtroTiempo === 'mes' ? 'bg-blue-50 dark:bg-blue-500/10 text-blue-600 dark:text-blue-400' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'" class="px-5 py-2 text-xs font-black rounded-lg transition-colors">
              Este Mes
            </button>
            <button @click="filtroTiempo = 'trimestre'" :class="filtroTiempo === 'trimestre' ? 'bg-blue-50 dark:bg-blue-500/10 text-blue-600 dark:text-blue-400' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'" class="px-5 py-2 text-xs font-black rounded-lg transition-colors">
              Trimestre
            </button>
            <button @click="filtroTiempo = 'anio'" :class="filtroTiempo === 'anio' ? 'bg-blue-50 dark:bg-blue-500/10 text-blue-600 dark:text-blue-400' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'" class="px-5 py-2 text-xs font-black rounded-lg transition-colors">
              Este Año
            </button>
          </div>

          <button @click="descargarExcel" class="hover:scale-105 transition-transform flex items-center gap-2 px-6 py-3 rounded-xl font-black text-sm shadow-lg bg-emerald-500 hover:bg-emerald-600 text-white shadow-emerald-500/30">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14 2 14 8 20 8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10 9 9 9 8 9"></polyline>
            </svg>
            Exportar Excel
          </button>
          <a href="/admin/inventario" class="flex items-center gap-2 px-6 py-3 rounded-xl font-black text-sm transition-colors bg-white dark:bg-slate-800 text-slate-700 dark:text-slate-300 border-2 border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-700">
            Volver
          </a>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- BANNER FINANCIERO (BLINDADO Y DINÁMICO)    -->
      <!-- ========================================== -->
      <div class="rounded-3xl p-8 md:p-10 mb-8 shadow-2xl relative overflow-hidden transition-colors duration-300" style="background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);">
        <div class="absolute top-0 right-0 w-96 h-96 rounded-full blur-3xl opacity-20 -translate-y-1/2 translate-x-1/2 pointer-events-none" style="background-color: #3b82f6;"></div>
        
        <div class="relative z-10 flex flex-col lg:flex-row justify-between items-start lg:items-center gap-8">
          <div>
            <span class="font-black tracking-[0.2em] uppercase text-xs" style="color: #94a3b8 !important;">Ingresos Netos ({{ textoFiltro }})</span>
            <div class="flex flex-wrap items-end gap-4 mt-2">
              <h3 class="text-5xl md:text-6xl font-black m-0 tracking-tight transition-opacity duration-300" :class="{'opacity-30 blur-sm scale-95': animandoMetricas}" style="color: #ffffff !important;">S/ {{ formatPrecio(ingresosNetos) }}</h3>
              <span v-if="ingresosNetos > 0" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-black mb-2 border transition-opacity duration-300" :class="{'opacity-0': animandoMetricas}" style="background: rgba(16, 185, 129, 0.15) !important; color: #34d399 !important; border-color: rgba(52, 211, 153, 0.3) !important;">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" width="12" height="12" style="color: #34d399 !important;"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"></polyline><polyline points="17 6 23 6 23 12"></polyline></svg>
                Métrica Real
              </span>
            </div>
          </div>
          
          <div class="flex flex-wrap gap-8 lg:gap-12">
            <div>
              <span class="font-black uppercase text-[10px] tracking-[0.2em] block mb-2" style="color: #64748b !important;">Ticket Promedio</span>
              <span class="text-3xl font-black font-mono transition-opacity duration-300" :class="{'opacity-30 blur-sm': animandoMetricas}" style="color: #ffffff !important;">S/ {{ formatPrecio(ticketPromedio) }}</span>
            </div>
            <div class="w-px h-16 hidden md:block" style="background-color: #334155;"></div>
            <div>
              <span class="font-black uppercase text-[10px] tracking-[0.2em] block mb-2" style="color: #64748b !important;">Tasa Conversión</span>
              <span class="text-3xl font-black font-mono transition-opacity duration-300" :class="{'opacity-30 blur-sm': animandoMetricas}" style="color: #ffffff !important;">{{ tasaConversion }}%</span>
            </div>
          </div>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- TARJETAS KPI OPERATIVAS                    -->
      <!-- ========================================== -->
      <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mb-8">
        
        <!-- Stock -->
        <div class="bg-white dark:bg-slate-800 rounded-3xl p-6 border border-slate-200 dark:border-slate-700 shadow-sm hover:shadow-xl hover:border-blue-300 dark:hover:border-blue-500 transition-all group relative overflow-hidden">
          <div class="w-14 h-14 rounded-2xl flex items-center justify-center mb-5 transition-transform group-hover:scale-110 bg-blue-50 dark:bg-blue-500/10 text-blue-600 dark:text-blue-400">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="28" height="28"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>
          </div>
          <div class="text-sm font-bold mb-1 text-slate-500 dark:text-slate-400">Stock Físico Total</div>
          <div class="text-4xl font-black text-slate-800 dark:text-white">{{ kpiTotalStock }}</div>
          <div class="mt-3 text-[11px] font-black tracking-widest uppercase text-blue-500 dark:text-blue-400">Unidades Activas</div>
        </div>

        <!-- Ventas -->
        <div class="bg-white dark:bg-slate-800 rounded-3xl p-6 border border-slate-200 dark:border-slate-700 shadow-sm hover:shadow-xl hover:border-emerald-300 dark:hover:border-emerald-500 transition-all group relative overflow-hidden">
          <div class="w-14 h-14 rounded-2xl flex items-center justify-center mb-5 transition-transform group-hover:scale-110 bg-emerald-50 dark:bg-emerald-500/10 text-emerald-600 dark:text-emerald-400">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="28" height="28"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg>
          </div>
          <div class="text-sm font-bold mb-1 text-slate-500 dark:text-slate-400">Ventas Concretadas</div>
          <div class="text-4xl font-black text-slate-800 dark:text-white transition-opacity duration-300" :class="{'opacity-30 blur-sm': animandoMetricas}">{{ ventasValidas.length }}</div>
          <div class="mt-3 text-[11px] font-black tracking-widest uppercase text-emerald-500 dark:text-emerald-400">Salidas Procesadas</div>
        </div>

        <!-- Mermas -->
        <div class="bg-white dark:bg-slate-800 rounded-3xl p-6 border border-slate-200 dark:border-slate-700 shadow-sm hover:shadow-xl hover:border-red-300 dark:hover:border-red-500 transition-all group relative overflow-hidden">
          <div class="w-14 h-14 rounded-2xl flex items-center justify-center mb-5 transition-transform group-hover:scale-110 bg-red-50 dark:bg-red-500/10 text-red-600 dark:text-red-400">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="28" height="28"><polygon points="7.86 2 16.14 2 22 7.86 22 16.14 16.14 22 7.86 22 2 16.14 2 7.86 7.86 2"></polygon><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>
          </div>
          <div class="text-sm font-bold mb-1 text-slate-500 dark:text-slate-400">Mermas / Anulaciones</div>
          <div class="text-4xl font-black text-slate-800 dark:text-white transition-opacity duration-300" :class="{'opacity-30 blur-sm': animandoMetricas}">{{ kpiMermasAnuladas }}</div>
          <div class="mt-3 text-[11px] font-black tracking-widest uppercase text-red-500 dark:text-red-400">Registros Defectuosos</div>
        </div>

        <!-- Alertas -->
        <div class="bg-white dark:bg-slate-800 rounded-3xl p-6 border border-slate-200 dark:border-slate-700 shadow-sm hover:shadow-xl hover:border-amber-300 dark:hover:border-amber-500 transition-all group relative overflow-hidden">
          <div class="w-14 h-14 rounded-2xl flex items-center justify-center mb-5 transition-transform group-hover:scale-110 bg-amber-50 dark:bg-amber-500/10 text-amber-600 dark:text-amber-400">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="28" height="28"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
          </div>
          <div class="text-sm font-bold mb-1 text-slate-500 dark:text-slate-400">Alertas Críticas</div>
          <div class="text-4xl font-black text-slate-800 dark:text-white">{{ kpiStockCritico }}</div>
          <div class="mt-3 text-[11px] font-black tracking-widest uppercase text-amber-500 dark:text-amber-400">SKUs Bajo el Mínimo</div>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- GRÁFICO DINÁMICO DE TENDENCIAS             -->
      <!-- ========================================== -->
      <div class="bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-3xl p-8 shadow-sm mb-8 transition-colors duration-300">
        <div class="flex justify-between items-center mb-8">
          <h3 class="text-xl font-black m-0 flex items-center gap-3 text-slate-800 dark:text-white">
            <div class="p-2 bg-indigo-50 dark:bg-indigo-500/10 rounded-lg text-indigo-500"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg></div>
            Tendencia de Ingresos Diarios
          </h3>
          <span class="text-xs font-black px-4 py-2 rounded-xl uppercase tracking-widest bg-slate-100 dark:bg-slate-700 text-slate-500 dark:text-slate-400">
            Filtro Activo: {{ textoFiltro }}
          </span>
        </div>
        <div style="position: relative; height: 320px; width: 100%;">
          <canvas id="revenueChart"></canvas>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- GRÁFICOS INFERIORES DINÁMICOS              -->
      <!-- ========================================== -->
      <div class="grid grid-cols-1 xl:grid-cols-5 gap-8 mb-8">
        
        <!-- Top 5 Productos (Barras) -->
        <div class="xl:col-span-3 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-3xl p-8 shadow-sm transition-colors duration-300">
          <div class="flex justify-between items-center mb-8">
            <h3 class="text-xl font-black m-0 flex items-center gap-3 text-slate-800 dark:text-white">
              <div class="p-2 bg-blue-50 dark:bg-blue-500/10 rounded-lg text-blue-500"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><line x1="18" y1="20" x2="18" y2="10"></line><line x1="12" y1="20" x2="12" y2="4"></line><line x1="6" y1="20" x2="6" y2="14"></line></svg></div>
              Top 5 Productos Más Vendidos
            </h3>
          </div>
          <div style="position: relative; height: 300px; width: 100%;">
              <canvas id="barChart"></canvas>
          </div>
        </div>
        
        <!-- Dona (Categorías) -->
        <div class="xl:col-span-2 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-3xl p-8 shadow-sm flex flex-col transition-colors duration-300">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-xl font-black m-0 flex items-center gap-3 text-slate-800 dark:text-white">
              <div class="p-2 bg-purple-50 dark:bg-purple-500/10 rounded-lg text-purple-500"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path d="M21.21 15.89A10 10 0 1 1 8 2.83"></path><path d="M22 12A10 10 0 0 0 12 2v10z"></path></svg></div>
              Distribución de Stock
            </h3>
          </div>
          <div style="position: relative; flex-grow: 1; width: 100%; display: flex; justify-content: center; align-items: center; min-height: 280px;">
              <canvas id="doughnutChart"></canvas>
          </div>
        </div>
      </div>

      <!-- ========================================== -->
      <!-- TABLA DE AUDITORÍA EN VIVO (CON VUE)       -->
      <!-- ========================================== -->
      <div class="bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-3xl overflow-hidden shadow-sm transition-colors duration-300">
        <div class="p-8 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row justify-between sm:items-center gap-4 bg-slate-50/50 dark:bg-slate-800/50">
          <h3 class="text-xl font-black m-0 flex items-center gap-3 text-slate-800 dark:text-white">
              <div class="p-2 bg-emerald-50 dark:bg-emerald-500/10 rounded-lg text-emerald-500"><svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M22 12h-4l-3 9L9 3l-3 9H2"></path></svg></div>
              Monitor de Transacciones en Vivo ({{ textoFiltro }})
          </h3>
          <span class="flex items-center gap-2 text-xs font-black uppercase tracking-widest px-4 py-2 rounded-xl bg-emerald-50 dark:bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-200 dark:border-emerald-500/20">
            <span class="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-pulse"></span> Sincronizado Real
          </span>
        </div>
        
        <div class="overflow-x-auto p-2" :class="{'opacity-50 blur-sm': animandoMetricas}" style="transition: all 0.3s ease;">
          <table class="w-full text-left border-collapse">
              <thead>
                  <tr class="text-[10px] font-black uppercase tracking-widest border-b-2 border-slate-100 dark:border-slate-700 text-slate-500 dark:text-slate-400">
                      <th class="p-5">Fecha / Hora</th>
                      <th class="p-5">Comprobante</th>
                      <th class="p-5">Cliente</th>
                      <th class="p-5">Producto</th>
                      <th class="p-5 text-right">Monto Total</th>
                      <th class="p-5 text-center">Estado</th>
                  </tr>
              </thead>
              <tbody class="divide-y divide-slate-50 dark:divide-slate-700/50">
                <tr v-if="ultimasVentas.length === 0">
                  <td colspan="6" class="text-center p-8 text-slate-500 dark:text-slate-400 font-bold">No hay transacciones registradas en este periodo de tiempo.</td>
                </tr>
                <tr v-for="venta in ultimasVentas" :key="venta.tipoComprobanteReal" class="hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors">
                  <td class="p-5 text-sm font-medium text-slate-600 dark:text-slate-300">{{ formatearFechaVisual(venta.fechaReal) }}</td>
                  <td class="p-5 text-sm font-black text-blue-600 dark:text-blue-400">{{ venta.tipoComprobanteReal }}</td>
                  <td class="p-5 text-sm font-bold text-slate-800 dark:text-slate-200">{{ venta.clienteReal }}</td>
                  <td class="p-5 text-sm font-medium text-slate-600 dark:text-slate-300">
                    <span v-if="venta.detallesReales.length === 1">{{ venta.detallesReales[0].nombreProducto }}</span>
                    <span v-else>Varios productos ({{ venta.detallesReales.length }})</span>
                  </td>
                  <td class="p-5 text-sm font-black text-right text-slate-800 dark:text-white">S/ {{ formatPrecio(venta.totalReal) }}</td>
                  <td class="p-5 text-center">
                    <span class="px-3 py-1.5 rounded-lg text-[10px] font-black uppercase tracking-widest border" :class="claseEstado(venta.estadoReal)">
                      {{ venta.estadoReal }}
                    </span>
                  </td>
                </tr>
              </tbody>
          </table>
        </div>
        
        <!-- ========================================== -->
        <!-- NUEVO: BOTÓN VER TODO EL HISTORIAL         -->
        <!-- ========================================== -->
        <div class="p-5 border-t border-slate-100 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-800/50 flex justify-center">
          <a href="/admin/historial-ventas" class="inline-flex items-center gap-2 px-6 py-2.5 rounded-full text-sm font-black transition-all bg-white dark:bg-slate-800 text-blue-600 dark:text-blue-400 border-2 border-blue-100 dark:border-blue-500/20 hover:bg-blue-50 dark:hover:bg-blue-500/10 hover:scale-105 shadow-sm">
            Ver todo el historial 
            <svg class="w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3"></path></svg>
          </a>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
import { apiClient } from '@/services/apiClient';

export default {
  name: 'ReportesView',
  data() {
    return {
      cargando: false, // Variable para activar la pantalla de carga
      filtroTiempo: 'trimestre', // 'mes', 'trimestre', 'anio'
      ventasGlobales: [],
      productosGlobales: [],
      categoriasGlobales: [],
      animandoMetricas: false, // Controla el parpadeo visual UX al hacer clic en filtros
      // 🎨 Paleta base y mapa fijo idCategoria -> color (el azul es siempre primero)
      paletaColores: [
        '#2563eb', '#8b5cf6', '#10b981', '#f59e0b', '#ef4444', '#ec4899', 
        '#0ea5e9', '#14b8a6', '#f97316', '#6366f1', '#a855f7', '#84cc16', 
        '#06b6d4', '#eab308', '#d946ef', '#64748b'
      ],
      coloresPorCategoria: {},
      _debounceGraficos: null
    }
  },
  computed: {
    textoFiltro() {
      if (this.filtroTiempo === 'mes') return 'Este Mes';
      if (this.filtroTiempo === 'trimestre') return 'Último Trimestre';
      return 'Este Año';
    },
    // Filtro maestro de tiempo que afecta TODO el tablero de ventas
    ventasFiltradas() {
      const hoy = new Date();
      return this.ventasGlobales.filter(v => {
        // Ignorar la venta si por algún error en la Base de Datos no tiene fecha válida
        if (!v.fechaReal || isNaN(v.fechaReal.getTime())) return false;
        
        const fechaVenta = v.fechaReal;
        if (this.filtroTiempo === 'mes') {
          return fechaVenta.getMonth() === hoy.getMonth() && fechaVenta.getFullYear() === hoy.getFullYear();
        } else if (this.filtroTiempo === 'trimestre') {
          // Últimos 90 días exactos
          const hace90Dias = new Date();
          hace90Dias.setDate(hoy.getDate() - 90);
          return fechaVenta >= hace90Dias && fechaVenta <= hoy;
        } else if (this.filtroTiempo === 'anio') {
          return fechaVenta.getFullYear() === hoy.getFullYear();
        }
        return true;
      });
    },
    // Descartamos ventas canceladas para la suma de dinero (Incluimos PENDIENTE para web)
    ventasValidas() {
      return this.ventasFiltradas.filter(v => ['COMPLETADA', 'ENTREGADO', 'ENVIADO', 'PAGADO', 'PENDIENTE'].includes(v.estadoReal));
    },
    // Ingresos Netos reales sumados de la Base de Datos
    ingresosNetos() {
      return this.ventasValidas.reduce((total, v) => total + Number(v.totalReal), 0);
    },
    ticketPromedio() {
      if (this.ventasValidas.length === 0) return 0;
      return this.ingresosNetos / this.ventasValidas.length;
    },
    tasaConversion() {
      if (this.ventasFiltradas.length === 0) return 0;
      const porcentaje = (this.ventasValidas.length / this.ventasFiltradas.length) * 100;
      return porcentaje.toFixed(1);
    },
    kpiTotalStock() {
       return this.productosGlobales.reduce((sum, p) => sum + (p.stockActual ?? p.stock ?? 0), 0);
    },
    kpiStockCritico() {
       return this.productosGlobales.filter(p => (p.stockActual ?? p.stock ?? 0) <= (p.stockMinimo ?? 5)).length;
    },
    // Mermas respetando el filtro de tiempo
    kpiMermasAnuladas() {
      return this.ventasFiltradas.filter(v => ['CANCELADO', 'ANULADA'].includes(v.estadoReal)).length;
    },
    // La tabla de abajo muestra siempre las últimas 5 transacciones del filtro actual (ordenadas matemáticamente por getTime)
    ultimasVentas() {
      return [...this.ventasFiltradas].sort((a,b) => b.fechaReal.getTime() - a.fechaReal.getTime()).slice(0, 5);
    }
  },
  watch: {
    // Escuchador automático: Activa pantalla de carga y redibuja los gráficos
    filtroTiempo() {
      this.cargando = true; // 1. Mostramos el overlay de carga bloqueando la pantalla

      clearTimeout(this._debounceGraficos);
      this._debounceGraficos = setTimeout(() => {
        this.actualizarGraficos(); // 2. Recalculamos los gráficos
        this.cargando = false; // 3. Ocultamos el overlay de carga
      }, 800); // El 800 simula un tiempo de carga de casi 1 segundo (ajusta si lo quieres más rápido o lento)
    }
  },
  // 🛡️ Variables No-Reactivas: Esto soluciona el error Maximum call stack y acelera el panel al 1000%
  created() {
    this.chartTendencia = null;
    this.chartBarras = null;
    this.chartDona = null;
  },
  
  mounted() {
    window.VITE_API_URL = import.meta.env.VITE_API_URL;

    Promise.all([
      this.cargarScriptExterno('https://cdn.jsdelivr.net/npm/chart.js'),
      this.cargarScriptExterno('https://cdn.jsdelivr.net/npm/xlsx/dist/xlsx.full.min.js')
    ])
    .then(() => this.cargarDatosReales())
    .catch(err => console.error("Error al inyectar infraestructura del BI Dashboard:", err));
  },
  beforeUnmount() {
    // Limpieza al salir de la vista
    clearTimeout(this._debounceGraficos);
    this.destruirGraficoSiExiste('revenueChart', this.chartTendencia);
    this.destruirGraficoSiExiste('barChart', this.chartBarras);
    this.destruirGraficoSiExiste('doughnutChart', this.chartDona);
  },
  methods: {
    formatPrecio(num) {
      return Number(num).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    formatearFechaVisual(fechaObj) {
      if (!fechaObj || isNaN(fechaObj.getTime())) return '-';
      return fechaObj.toLocaleDateString('es-PE', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    },
    claseEstado(estado) {
      if (['ENTREGADO', 'COMPLETADA', 'PAGADO'].includes(estado)) return 'bg-emerald-50 text-emerald-600 border-emerald-200 dark:bg-emerald-500/10 dark:text-emerald-400 dark:border-emerald-500/20';
      if (['ENVIADO'].includes(estado)) return 'bg-blue-50 text-blue-600 border-blue-200 dark:bg-blue-500/10 dark:text-blue-400 dark:border-blue-500/20';
      if (['PENDIENTE', 'PROCESANDO'].includes(estado)) return 'bg-amber-50 text-amber-600 border-amber-200 dark:bg-amber-500/10 dark:text-amber-400 dark:border-amber-500/20';
      return 'bg-red-50 text-red-500 border-red-200 dark:bg-red-500/10 dark:text-red-400 dark:border-red-500/20'; 
    },
    cargarScriptExterno(url) {
      return new Promise((resolve, reject) => {
        const existente = document.querySelector(`script[src="${url}"]`);
        if (existente) { resolve(); return; }
        const script = document.createElement('script');
        script.src = url;
        script.onload = resolve;
        script.onerror = reject;
        document.body.appendChild(script);
      });
    },

    // 💡 EXCEL CORPORATIVO
    async descargarExcel() {
      try {
        if (!window.XLSX) {
          await this.cargarScriptExterno('https://cdn.jsdelivr.net/npm/xlsx/dist/xlsx.full.min.js');
        }

        const blob = await apiClient.descargarReporteExcel();
        const arrayBuffer = await blob.arrayBuffer();
        const wb = window.XLSX.read(arrayBuffer, { type: 'array' });

        const kpis = [
          { Metrica: "Filtro Seleccionado", Valor: this.textoFiltro },
          { Metrica: "Ingresos Netos (S/)", Valor: this.ingresosNetos.toFixed(2) },
          { Metrica: "Ventas Concretadas", Valor: this.ventasValidas.length },
          { Metrica: "Ticket Promedio (S/)", Valor: this.ticketPromedio.toFixed(2) },
          { Metrica: "Tasa Conversión (%)", Valor: this.tasaConversion },
          { Metrica: "Mermas / Anulaciones", Valor: this.kpiMermasAnuladas },
          { Metrica: "SKUs Críticos", Valor: this.kpiStockCritico }
        ];

        const detalleVentas = this.ventasFiltradas.map(v => ({
          "Fecha y Hora": this.formatearFechaVisual(v.fechaReal),
          "Tipo Comprobante": v.tipoComprobanteReal,
          "Cliente": v.clienteReal,
          "Monto Total (S/)": v.totalReal,
          "Estado": v.estadoReal,
          "Productos": v.detallesReales.map(d => `${d.cantidad}x ${d.nombreProducto}`).join(', ')
        }));

        const wsKpis = window.XLSX.utils.json_to_sheet(kpis);
        const wsVentas = window.XLSX.utils.json_to_sheet(detalleVentas);

        window.XLSX.utils.book_append_sheet(wb, wsKpis, "KPIs Generales");
        window.XLSX.utils.book_append_sheet(wb, wsVentas, "Historial de Ventas");

        window.XLSX.writeFile(wb, `Reporte_Maestro_${this.filtroTiempo.toUpperCase()}.xlsx`);

      } catch (error) {
        console.error(error);
        alert("Ocurrió un error al intentar generar el Excel combinado. Verifique la conexión.");
      }
    },

    asignarColoresCategorias() {
      const mapa = {};
      [...this.categoriasGlobales]
        .sort((a, b) => Number(a.idCategoria) - Number(b.idCategoria))
        .forEach((cat, i) => {
          mapa[String(cat.idCategoria)] = this.paletaColores[i % this.paletaColores.length];
        });
      mapa['otros'] = '#94a3b8';
      this.coloresPorCategoria = mapa;
    },
    colorDeCategoria(idCategoria) {
      return this.coloresPorCategoria[String(idCategoria)] || this.coloresPorCategoria['otros'];
    },

    destruirGraficoSiExiste(canvasId, chartRef) {
      try {
        if (chartRef && typeof chartRef.destroy === 'function') {
          chartRef.destroy();
        }
      } catch (e) {}
      
      try {
        if (window.Chart && typeof window.Chart.getChart === 'function') {
          const existente = window.Chart.getChart(canvasId);
          if (existente) existente.destroy();
        }
      } catch (e) {}
    },

    // 💡 LÓGICA DE NEGOCIOS REAL
    async cargarDatosReales() {
      try {
        const [web, pos, prods, cats] = await Promise.all([
          apiClient.obtenerTodosLosPedidosWeb().catch(()=>[]),
          apiClient.obtenerHistorialPOS().catch(()=>[]),
          apiClient.obtenerProductos().catch(()=>[]),
          apiClient.obtenerCategorias().catch(()=>[])
        ]);
        
        this.productosGlobales = prods;
        this.categoriasGlobales = cats;
        this.asignarColoresCategorias();

        // ARREGLO BLINDADO PARA LAS FECHAS (Soporta Arrays de Spring Boot y Strings de BD)
        const mapearData = (venta, tipo) => {
          let fechaNormalizada;
          const fechaCruda = tipo === 'WEB' ? venta.fechaPedido : (venta.fecha || venta.fechaVenta || venta.createdAt);
          
          if (Array.isArray(fechaCruda)) {
            fechaNormalizada = new Date(
              fechaCruda[0], 
              (fechaCruda[1] || 1) - 1, 
              fechaCruda[2] || 1, 
              fechaCruda[3] || 0, 
              fechaCruda[4] || 0
            );
          } else {
            fechaNormalizada = new Date(fechaCruda);
          }

          return {
            ...venta,
            fechaReal: fechaNormalizada,
            totalReal: Number(venta.total || 0),
            estadoReal: venta.estado,
            detallesReales: tipo === 'WEB' ? (venta.detalles || []) : [{ nombreProducto: venta.nombreProducto, cantidad: 1 }],
            tipoComprobanteReal: tipo === 'WEB' ? 'WEB-' + venta.nroPedido : 'POS-' + venta.nroComprobante,
            clienteReal: venta.nombreCliente || 'Cliente General'
          };
        };

        this.ventasGlobales = [
          ...web.map(v => mapearData(v, 'WEB')),
          ...pos.map(v => mapearData(v, 'POS'))
        ];

        this.actualizarGraficos();

      } catch (error) {
        console.error("No se pudo cargar la data financiera real", error);
      }
    },

    actualizarGraficos() {
      this.renderizarGraficoTendencia(true);
      this.renderizarGraficoBarras(true);
      if (!this.chartDona) this.renderizarGraficoDona();
    },

    // 💡 MOTOR DE GRÁFICOS: TENDENCIA
    renderizarGraficoTendencia(esActualizacion = false) {
      if (!window.Chart) return;

      const ventasOrdenadas = [...this.ventasValidas].sort((a,b) => a.fechaReal.getTime() - b.fechaReal.getTime());

      const agrupacionFechas = new Map();
      ventasOrdenadas.forEach(v => {
        const dateObj = v.fechaReal;
        const etiqueta = this.filtroTiempo === 'anio' 
          ? dateObj.toLocaleDateString('es-PE', { month: 'short', year: 'numeric' })
          : dateObj.toLocaleDateString('es-PE', { day: '2-digit', month: 'short' });

        const valorActual = agrupacionFechas.get(etiqueta) || 0;
        agrupacionFechas.set(etiqueta, valorActual + v.totalReal);
      });

      let labels = Array.from(agrupacionFechas.keys());
      let data = Array.from(agrupacionFechas.values());

      if (labels.length === 0) { labels = ['Sin Datos']; data = [0]; }

      if (esActualizacion && this.chartTendencia) {
        this.chartTendencia.data.labels = labels;
        this.chartTendencia.data.datasets[0].data = data;
        this.chartTendencia.update(); 
        return;
      }

      const ctx = document.getElementById('revenueChart');
      if (!ctx) return;

      this.chartTendencia = new window.Chart(ctx, {
        type: 'line',
        data: {
          labels,
          datasets: [{
            label: 'Ingresos Netos (S/)',
            data,
            borderColor: '#6366f1', 
            backgroundColor: 'rgba(99, 102, 241, 0.15)', 
            borderWidth: 4,
            pointBackgroundColor: '#ffffff',
            pointBorderColor: '#6366f1',
            pointBorderWidth: 3,
            pointRadius: 5,
            fill: true,
            tension: 0.4 
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          animation: { duration: 300 },
          plugins: { legend: { display: false } },
          scales: {
            y: {
              beginAtZero: true,
              grid: { color: 'rgba(148, 163, 184, 0.1)', drawBorder: false, borderDash: [5, 5] },
              ticks: { color: '#94a3b8', font: { size: 12, weight: 'bold' } }
            },
            x: {
              grid: { display: false },
              ticks: { color: '#94a3b8', font: { size: 12, weight: 'bold' } }
            }
          }
        }
      });
    },

    // 💡 MOTOR DE GRÁFICOS: BARRAS
    renderizarGraficoBarras(esActualizacion = false) {
      if (!window.Chart) return;
      
      const conteo = {};
      this.ventasValidas.forEach(v => {
        v.detallesReales.forEach(d => {
          if (!conteo[d.nombreProducto]) {
            const prod = this.productosGlobales.find(
              p => p.nombre === d.nombreProducto || p.nombreProducto === d.nombreProducto
            );
            conteo[d.nombreProducto] = { cantidad: 0, idCategoria: prod ? prod.idCategoria : 'otros' };
          }
          conteo[d.nombreProducto].cantidad += d.cantidad;
        });
      });
      
      const sorted = Object.entries(conteo).sort((a,b) => b[1].cantidad - a[1].cantidad).slice(0, 5);
      const labels = sorted.length ? sorted.map(([n]) => n.length > 18 ? n.substring(0, 18) + '...' : n) : ['Sin Datos'];
      const data = sorted.length ? sorted.map(([, v]) => v.cantidad) : [0];
      const colores = sorted.length ? sorted.map(([, v]) => this.colorDeCategoria(v.idCategoria)) : ['#3b82f6'];

      if (esActualizacion && this.chartBarras) {
        this.chartBarras.data.labels = labels;
        this.chartBarras.data.datasets[0].data = data;
        this.chartBarras.data.datasets[0].backgroundColor = colores;
        this.chartBarras.update();
        return;
      }

      const ctx = document.getElementById('barChart');
      if (!ctx) return;

      this.chartBarras = new window.Chart(ctx, {
        type: 'bar',
        data: {
          labels,
          datasets: [{
            label: 'Unidades Vendidas',
            data,
            backgroundColor: colores,
            borderRadius: 6
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          animation: { duration: 300 },
          plugins: { legend: { display: false } },
          scales: {
             y: { beginAtZero: true, grid: { color: 'rgba(148, 163, 184, 0.1)', borderDash: [5, 5] }, ticks: {color: '#94a3b8'} },
             x: { grid: { display: false }, ticks: {color: '#94a3b8', font: {size: 11, weight: 'bold'}} }
          }
        }
      });
    },

    // 💡 MOTOR DE GRÁFICOS: DONA CATEGORÍAS
    renderizarGraficoDona() {
      if (!window.Chart) return;
      this.destruirGraficoSiExiste('doughnutChart', this.chartDona);
      this.chartDona = null;
      
      const conteo = {};
      this.productosGlobales.forEach(p => {
        const cat = this.categoriasGlobales.find(c => String(c.idCategoria) === String(p.idCategoria));
        const id = cat ? String(cat.idCategoria) : 'otros';
        const nombre = cat ? cat.nombre : 'Otros';
        if (!conteo[id]) conteo[id] = { nombre, total: 0 };
        conteo[id].total += (p.stockActual ?? p.stock ?? 0);
      });
      
      const entradas = Object.entries(conteo);
      const labels = entradas.length ? entradas.map(([, v]) => v.nombre) : ['Sin Stock'];
      const data = entradas.length ? entradas.map(([, v]) => v.total) : [1];
      const colores = entradas.length ? entradas.map(([id]) => this.colorDeCategoria(id)) : ['#94a3b8'];

      const ctx = document.getElementById('doughnutChart');
      if (!ctx) return;

      this.chartDona = new window.Chart(ctx, {
        type: 'doughnut',
        data: {
          labels,
          datasets: [{
            data,
            backgroundColor: colores,
            borderWidth: 0,
            hoverOffset: 4
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          animation: { duration: 300 },
          cutout: '75%',
          plugins: {
             legend: { position: 'right', labels: { color: '#94a3b8', font: {family: 'sans-serif', weight: 'bold'} } }
          }
        }
      });
    }
  }
}
</script>