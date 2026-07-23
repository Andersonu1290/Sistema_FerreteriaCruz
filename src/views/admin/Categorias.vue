<template>
  <div>
    <div class="dashboard-container">
        
        <div class="header-tech">
            <div class="header-title">
                <h2 class="page-title text-white flex items-center gap-2">
                    <svg viewBox="0 0 24 24" fill="none" stroke="var(--brand-blue)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24">
                        <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"></path>
                    </svg>
                    Categorías de Producto
                </h2>
            </div>
            <a href="/admin/producto-form" class="btn-tech">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="16" height="16" class="mr-2">
                    <line x1="19" y1="12" x2="5" y2="12"></line>
                    <polyline points="12 19 5 12 12 5"></polyline>
                </svg>
                Volver a Registro de Producto
            </a>
        </div>

        <div class="grid-container mt-20">
            
            <div class="form-side">
                <h3 class="subtitle-blue">Crear Nueva Familia</h3>
                
                <!-- 100% Controlado por Vue (@submit.prevent evita la recarga de página) -->
                <form @submit.prevent="guardarCategoria">
                    <label class="form-label">Nombre de la Categoría:</label>
                    <input 
                        type="text" 
                        v-model="nuevaCategoria" 
                        class="input-tech" 
                        required 
                        placeholder="Ej. Construccion, Herramientas..." 
                        autocomplete="off"
                        :disabled="procesando"
                    >
                    
                    <button type="submit" class="btn-submit-tech flex justify-center items-center gap-2" :disabled="procesando || !nuevaCategoria.trim()">
                        <span v-if="procesando" class="animate-spin inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full"></span>
                        {{ procesando ? 'Guardando...' : 'Guardar Categoría' }}
                    </button>
                </form>
                
                <div class="note-box mt-20">
                    <p>
                        <strong>Nota:</strong> Las categorías creadas aquí aparecerán automáticamente en el menú desplegable al registrar un nuevo componente en el inventario.
                    </p>
                </div>
            </div>

            <div class="table-side">
                <div class="d-flex justify-between align-center mb-15">
                    <h3 class="text-white font-bold">Familias Registradas</h3>
                </div>

                <div class="search-box-container">
                    <svg viewBox="0 0 24 24" fill="none" stroke="#6b7280" stroke-width="2" width="16" height="16">
                        <circle cx="11" cy="11" r="8"></circle>
                        <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                    </svg>
                    <!-- Filtro en vivo con Vue (v-model) -->
                    <input type="text" v-model="busqueda" class="input-tech" placeholder="Buscar familia...">
                </div>

                <div class="table-panel">
                    <table class="tech-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>NOMBRE DE FAMILIA / CATEGORÍA</th>
                                <th class="text-center">ACCIONES</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Estado de carga -->
                            <tr v-if="cargando">
                                <td colspan="3" class="text-center p-10">
                                    <span class="animate-spin inline-block w-8 h-8 border-[4px] border-blue-500 border-t-transparent rounded-full mb-2"></span>
                                    <p class="text-slate-500 font-bold text-sm">Consultando base de datos...</p>
                                </td>
                            </tr>
                            
                            <!-- Estado vacío -->
                            <tr v-else-if="categoriasFiltradas.length === 0">
                                <td colspan="3" class="text-center p-10">
                                    <p class="text-slate-500 font-bold">No se encontraron categorías.</p>
                                </td>
                            </tr>

                            <!-- Listado iterado por Vue -->
                            <tr v-for="cat in categoriasFiltradas" :key="cat.idCategoria" class="hover:bg-slate-50 transition-colors">
                                <td class="font-mono text-slate-500 font-bold">#{{ cat.idCategoria }}</td>
                                <td class="font-black text-slate-800 uppercase tracking-wide">{{ cat.nombre }}</td>
                                <td class="text-center">
                                    <button @click="eliminarCategoria(cat.idCategoria)" class="px-4 py-2 text-xs font-black uppercase tracking-widest rounded-lg bg-red-50 border-2 border-red-500 text-red-600 hover:bg-red-500 hover:text-white transition-all active:scale-95 shadow-sm">
                                        Eliminar
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
  </div>
</template>

<script>
import { apiClient } from '@/services/apiClient';

export default {
  name: 'CategoriasView',
  data() {
    return {
      categorias: [],
      nuevaCategoria: '',
      busqueda: '',
      procesando: false,
      cargando: true
    };
  },
  computed: {
    // Filtro reactivo en tiempo real
    categoriasFiltradas() {
      if (!this.busqueda.trim()) return this.categorias;
      const term = this.busqueda.toLowerCase();
      return this.categorias.filter(c => c.nombre && c.nombre.toLowerCase().includes(term));
    }
  },
  mounted() {
    // Cargamos los datos al iniciar la pantalla
    this.cargarCategorias();
  },
  methods: {
    // 1. OBTENER CATEGORÍAS
    async cargarCategorias() {
      this.cargando = true;
      try {
        const data = await apiClient.obtenerCategorias();
        // Ordenamos las categorías por ID para que se vean organizadas
        this.categorias = data.sort((a, b) => a.idCategoria - b.idCategoria);
      } catch (error) {
        console.error("Error al cargar categorías:", error);
      } finally {
        this.cargando = false;
      }
    },

    // 2. CREAR CATEGORÍA (Blindado contra dobles clics)
    async guardarCategoria() {
      if (!this.nuevaCategoria.trim() || this.procesando) return;
      
      this.procesando = true;
      try {
        const baseUrl = (import.meta.env.VITE_API_URL || '/api') + '/v1';
        
        // Obtenemos el token de seguridad del administrador
        const tokenStr = sessionStorage.getItem('usuarioActivo');
        const token = sessionStorage.getItem('jwt_token') || sessionStorage.getItem('token') || (tokenStr ? JSON.parse(tokenStr).token : '');

        const respuesta = await fetch(`${baseUrl}/categorias`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({ nombre: this.nuevaCategoria })
        });

        if (!respuesta.ok) throw new Error("Error al guardar en el backend");

        // Éxito: Limpiamos la caja de texto y recargamos la tabla
        this.nuevaCategoria = '';
        await this.cargarCategorias();
        
      } catch (error) {
        console.error(error);
        alert("Hubo un problema al crear la categoría. Verifica la conexión.");
      } finally {
        this.procesando = false;
      }
    },

    // 3. ELIMINAR CATEGORÍA
    async eliminarCategoria(id) {
      if (!confirm("¿Estás completamente seguro de eliminar esta categoría?")) return;

      try {
        const baseUrl = (import.meta.env.VITE_API_URL || '/api') + '/v1';
        const tokenStr = sessionStorage.getItem('usuarioActivo');
        const token = sessionStorage.getItem('jwt_token') || sessionStorage.getItem('token') || (tokenStr ? JSON.parse(tokenStr).token : '');

        const respuesta = await fetch(`${baseUrl}/categorias/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!respuesta.ok) throw new Error("No se pudo eliminar");

        // Recargamos la tabla para ver el cambio reflejado
        await this.cargarCategorias();

      } catch (error) {
        alert("No se puede eliminar la categoría porque ya tiene productos asociados en el inventario.");
      }
    }
  }
}
</script>

<style scoped>
/* Estilos locales del módulo si fuesen requeridos */
</style>