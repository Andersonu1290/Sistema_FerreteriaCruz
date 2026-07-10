<template>
  <div class="admin-container min-h-screen flex flex-col bg-gray-50 text-gray-900 dark:bg-gray-900 dark:text-gray-100 transition-colors duration-300">
    
    <div class="flex-1">
      <router-view />
    </div>

    <button
      @click="toggleTheme"
      class="btn-flotante-tema"
      title="Cambiar Modo Día/Noche"
    >
      <svg v-if="!isDark" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" style="width: 24px; height: 24px;">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v2.25m6.364.386-1.591 1.591M21 12h-2.25m-.386 6.364-1.591-1.591M12 18.75V21m-4.773-4.227-1.591 1.591M5.25 12H3m4.227-4.773L5.636 5.636M15.75 12a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z" />
      </svg>
      <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" style="width: 24px; height: 24px;">
        <path stroke-linecap="round" stroke-linejoin="round" d="M21.752 15.002A9.72 9.72 0 0 1 18 15.75c-5.385 0-9.75-4.365-9.75-9.75 0-1.33.266-2.597.748-3.752A9.753 9.753 0 0 0 3 11.25C3 16.635 7.365 21 12.75 21a9.753 9.753 0 0 0 9.002-5.998Z" />
      </svg>
    </button>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const isDark = ref(false)

// Cuando se monta el Layout (al entrar al Admin), revisamos si ya había un tema guardado
onMounted(() => {
  const temaGuardado = localStorage.getItem('admin-theme')
  
  if (temaGuardado === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
  } else if (temaGuardado === 'light') {
    isDark.value = false
    document.documentElement.classList.remove('dark')
  } else {
    // Si es la primera vez, puedes elegir basarlo en el sistema del usuario:
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDark.value = true
      document.documentElement.classList.add('dark')
    }
  }
})

// Función para alternar entre blanco bonito y oscuro elegante
const toggleTheme = () => {
  isDark.value = !isDark.value
  
  if (isDark.value) {
    document.documentElement.classList.add('dark')
    localStorage.setItem('admin-theme', 'dark')
  } else {
    document.documentElement.classList.remove('dark')
    localStorage.setItem('admin-theme', 'light')
  }
}
</script>