import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import ProductCard from '../src/components/public/ProductCard.vue';
import { carritoStore } from '../src/store/carrito';
import { authStore } from '../src/store/auth'; 

describe('ProductCard Component', () => {
  it('Debe disparar la función agregarBD al hacer clic en el botón (+)', async () => {
    // 1. Espiamos directamente la función agregarBD de la store usando vi.spyOn
    const spy = vi.spyOn(carritoStore, 'agregarBD').mockImplementation(() => Promise.resolve());
    
    // 2. Simulamos la sesión activa del cliente
    authStore.usuarioActual = { idUsuario: 1 };
    
    // Forzamos el estado logueado simulando el getter o la propiedad necesaria
    vi.spyOn(authStore, 'estaLogueado', 'get').mockReturnValue(true);

    // Evitamos que los alert() nativos interfieran en la prueba
    vi.spyOn(window, 'alert').mockImplementation(() => {});

    // 3. Montamos el componente con un router simulado
    const wrapper = mount(ProductCard, {
      props: {
        producto: { idProducto: 1, nombre: 'Paracetamol', precio: 5.00, codigoSKU: 'MED-001', stock: 10 }
      },
      global: {
        stubs: {
          'router-link': true
        },
        mocks: {
          $router: {
            push: vi.fn()
          }
        }
      }
    });

    // 4. Buscamos el botón de incremento (+) y simulamos el clic
    const boton = wrapper.find('button');
    await boton.trigger('click');
    
    // Esperamos a que se resuelvan las promesas pendientes del ciclo de Vue
    await wrapper.vm.$nextTick();

    // 5. Verificamos que se haya llamado a la función de la base de datos
    expect(spy).toHaveBeenCalled();

    // 6. Restauramos los espías
    spy.mockRestore();
  });
});