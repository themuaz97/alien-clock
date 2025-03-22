<template>
  <Teleport to="body">
    <transition name="fade" appear>
      <div 
        v-if="isOpen" 
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" 
        @click.self="closeModal"
      >
        <div 
          class="bg-zinc-800 rounded-2xl shadow-xl max-w-lg w-full p-6 relative" 
          @keydown.esc="closeModal" 
          tabindex="0"
        >
          <button 
            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700" 
            @click="closeModal"
          >
            <i class="pi pi-times"></i>
          </button>
          <h3 class="text-xl font-semibold mb-4">{{ header }}</h3>
          <slot />
        </div>
      </div>
    </transition>
  </Teleport>
</template>

<script setup>
import { defineProps, defineEmits, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  header: {
    type: String,
    default: 'Modal Header',
  },
});

const emit = defineEmits(['update:isOpen']);

const closeModal = () => {
  emit('update:isOpen', false);
};

const handleEscape = (e) => {
  if (e.key === 'Escape') closeModal();
};

onMounted(() => document.addEventListener('keydown', handleEscape));
onBeforeUnmount(() => document.removeEventListener('keydown', handleEscape));
</script>

<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
