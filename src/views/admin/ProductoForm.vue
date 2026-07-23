<template>
  <div>
    <div class="dashboard-container-narrow">
        
        <div class="header-tech">
            <div class="header-title">
                <h2>
                    <svg viewBox="0 0 24 24" fill="none" stroke="var(--brand-blue)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="width: 24px; height: 24px;">
                        <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
                    </svg>
                    <span>{{ esEdicion ? 'Editar Componente' : 'Registrar Nuevo Producto' }}</span>
                </h2>
            </div>
            <a href="/admin/inventario" class="btn-tech">Cancelar y Volver</a>
        </div>

        <div class="form-panel">
            <form @submit.prevent="guardarProducto">
                
                <input type="hidden" v-model="form.idProducto">

                <label class="form-label">Código SKU único:</label>
                <input type="text" v-model="form.codigoSKU" class="input-tech input-mono" required placeholder="Ej. CER01" autocomplete="off" :disabled="procesando">

                <label class="form-label">Nombre / Especificación del Producto:</label>
                <input type="text" v-model="form.nombre" class="input-tech" required placeholder="Ej. Tubos" autocomplete="off" :disabled="procesando">
                
                <label class="form-label">Familia / Categoría del Componente:</label>
                <div class="d-flex align-center gap-15 mb-25">
                    <select v-model="form.idCategoria" class="input-tech flex-col" style="margin-bottom: 0;" required :disabled="procesando">
                        <option value="" disabled>Seleccione una familia...</option>
                        <option v-for="cat in categorias" :key="cat.idCategoria" :value="cat.idCategoria">
                            {{ cat.nombre }}
                        </option>
                    </select>
                    <a href="/admin/categorias" class="btn-action btn-edit" style="padding: 14px 20px; font-size: 14px;">Gestionar Categorías</a>
                </div>

                <div class="flex-row gap-15">
                    <div class="flex-col">
                        <label class="form-label">Stock Inicial/Actual:</label>
                        <input type="number" v-model.number="form.stockActual" class="input-tech input-mono" required min="0" :disabled="procesando">
                    </div>
                    <div class="flex-col">
                        <label class="form-label">Umbral de Alerta Mínimo:</label>
                        <input type="number" v-model.number="form.stockMinimo" class="input-tech input-mono" required min="0" :disabled="procesando">
                    </div>
                </div>

                <label class="form-label">Precio Unitario (S/):</label>
                <input type="number" v-model.number="form.precio" class="input-tech input-mono" step="0.01" required placeholder="Ej. 1500.50" :disabled="procesando">

                <label class="form-label mt-15">Fotografía del Componente (Opcional):</label>
                <input type="file" @change="capturarArchivo" class="input-tech" accept="image/png, image/jpeg, image/webp" :disabled="procesando">
                
                <!-- PREVISUALIZACIÓN DE LA IMAGEN ACTUAL O NUEVA -->
                <div class="info-box mt-15" v-if="urlImagenActual || archivoSeleccionado" style="display: flex; flex-direction: column; align-items: flex-start; padding: 15px;">
                    <img :src="urlImagenActual" class="product-img" style="width: auto; height: 100px; padding: 0; object-fit: contain;" alt="Vista previa">
                    <p class="text-muted text-xs font-bold" style="margin: 10px 0 0 0;">
                        {{ archivoSeleccionado ? 'Nueva imagen seleccionada' : 'Imagen Actual en el Sistema' }}
                    </p>
                </div>
                
                <button type="submit" class="btn-submit-tech mt-20 flex justify-center items-center gap-2" :disabled="procesando">
                    <span v-if="procesando" class="animate-spin inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full"></span>
                    {{ procesando ? 'Guardando...' : (esEdicion ? 'Actualizar Producto' : 'Guardar Nuevo Producto') }}
                </button>
            </form>
        </div>
    </div>
  </div>
</template>

<script>
import { apiClient } from '@/services/apiClient';

export default {
  name: 'ProductoFormView',
  data() {
    return {
      esEdicion: false,
      categorias: [],
      archivoSeleccionado: null,
      previewTemporal: null,
      procesando: false,
      form: {
        idProducto: '',
        codigoSKU: '',
        nombre: '',
        idCategoria: '',
        stockActual: 0,
        stockMinimo: 5,
        precio: ''
      }
    };
  },
  computed: {
    urlImagenActual() {
      // Si el usuario seleccionó un archivo nuevo, mostramos la previsualización local temporal
      if (this.previewTemporal) return this.previewTemporal;
      // Si estamos editando y el producto tiene ID, usamos la ruta oficial de tu apiClient
      if (this.esEdicion && this.form.idProducto) {
        return apiClient.obtenerUrlImagen(this.form.idProducto);
      }
      return '';
    }
  },
  async mounted() {
    window.VITE_API_URL = import.meta.env.VITE_API_URL;

    // 1. Cargamos las categorías para el selector
    try {
      this.categorias = await apiClient.obtenerCategorias();
    } catch (e) {
      console.error("Error al cargar categorías", e);
    }

    // 2. Revisamos si viene un ?id= en la URL para saber si es edición
    const urlParams = new URLSearchParams(window.location.search);
    const idProducto = urlParams.get('id');

    if (idProducto) {
      this.esEdicion = true;
      try {
        const producto = await apiClient.obtenerProductoPorId(idProducto);
        if (producto) {
          this.form.idProducto = producto.idProducto;
          this.form.codigoSKU = producto.codigoSKU || '';
          this.form.nombre = producto.nombre || '';
          this.form.idCategoria = producto.idCategoria || '';
          this.form.stockActual = producto.stockActual ?? producto.stock ?? 0;
          this.form.stockMinimo = producto.stockMinimo ?? 5;
          this.form.precio = producto.precio || 0;
        }
      } catch (error) {
        console.error("Error al cargar los datos del producto a editar:", error);
      }
    }
  },
  methods: {
    capturarArchivo(event) {
      const file = event.target.files[0];
      if (file) {
        this.archivoSeleccionado = file;
        this.previewTemporal = URL.createObjectURL(file);
      }
    },
    async guardarProducto() {
      if (this.procesando) return;
      this.procesando = true;

      try {
        const baseUrl = (import.meta.env.VITE_API_URL || '/api') + '/v1';
        const tokenStr = sessionStorage.getItem('usuarioActivo');
        const token = sessionStorage.getItem('jwt_token') || sessionStorage.getItem('token') || (tokenStr ? JSON.parse(tokenStr).token : '');

        // Usamos FormData para enviar archivos binarios (Multipart/Form-Data)
        const formData = new FormData();
        formData.append('codigoSKU', this.form.codigoSKU);
        formData.append('nombre', this.form.nombre);
        formData.append('idCategoria', this.form.idCategoria);
        formData.append('stockActual', this.form.stockActual);
        formData.append('stockMinimo', this.form.stockMinimo);
        formData.append('precio', this.form.precio);

        if (this.archivoSeleccionado) {
          formData.append('imagenFile', this.archivoSeleccionado);
        }

        let url = `${baseUrl}/productos`;
        let method = 'POST';

        if (this.esEdicion) {
          url = `${baseUrl}/productos/${this.form.idProducto}`;
          method = 'PUT';
        }

        const respuesta = await fetch(url, {
          method: method,
          headers: {
            'Authorization': `Bearer ${token}`
            // Nota: No se pone 'Content-Type' cuando se usa FormData, el navegador lo calcula solo.
          },
          body: formData
        });

        if (!respuesta.ok) {
          const errData = await respuesta.json().catch(() => null);
          throw new Error(errData?.error || 'Error al guardar el producto en el servidor.');
        }

        // Redirigir de regreso al inventario tras el éxito
        window.location.href = '/admin/inventario';

      } catch (error) {
        console.error(error);
        alert(error.message || "Ocurrió un error al procesar el formulario.");
      } finally {
        this.procesando = false;
      }
    }
  }
}
</script>

<style scoped>
/* Estilos específicos si el módulo del formulario lo requiere */
</style>