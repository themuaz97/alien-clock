<template>
  <div class="cards flex flex-col-reverse items-center gap-12">
    <!-- Earth Clock -->
    <div class="card flex flex-col justify-center items-center gap-4" data-alien-time-subscriber>
      <div class="relative">
        <h1 class="text-2xl font-semibold">Earth Clock</h1>
        <h2 class="flex gap-2 text-7xl font-bold">
          <span>{{ alienTimeStore.earthTimeParts[0] }}</span> :
          <span>{{ alienTimeStore.earthTimeParts[1] }}</span> :
          <span>{{ alienTimeStore.earthTimeParts[2] }}</span>
        </h2>
      </div>
      <span>{{ alienTimeStore.earthFormattedDate }} - {{ alienTimeStore.earthTimeZone }}</span>
    </div>
    <!-- Alien Clock -->
    <div class="card flex flex-col justify-center items-center gap-4">
      <div class="relative">
        <h1 class="text-2xl font-semibold">Alien Clock</h1>
        <h2 class="flex gap-2 text-7xl font-bold">
          <span>{{ alienTimeStore.padZero(alienTimeStore.alienTime.hour) }}</span> :
          <span>{{ alienTimeStore.padZero(alienTimeStore.alienTime.minute) }}</span> :
          <span>{{ alienTimeStore.padZero(alienTimeStore.alienTime.second) }}</span>
        </h2>
      </div>
      <div class="flex">
        <span>Day {{ alienTimeStore.alienTime.day }}, Month {{ alienTimeStore.alienTime.month }}, Year {{ alienTimeStore.alienTime.year }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import { useAlienTimeStore } from '@/stores/alienTime';

// Use the store
const alienTimeStore = useAlienTimeStore();

onMounted(() => {
  // Start the timer when component is mounted
  alienTimeStore.startTimer();
});

onUnmounted(() => {
  // Clean up when component is unmounted
  alienTimeStore.cleanupTimer();
});
</script>