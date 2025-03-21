<!-- @format -->

<template>
  <div class="flex flex-col items-center justify-center mt-5">
    <Tab :tabs="tabs" :activeTab="1" />
  </div>
</template>

<script setup>
import Tab from "@/components/Tab.vue";
import Alarm from "@/views/Alarm.vue";
import Clock from "@/views/Clock.vue";
import Converter from "@/views/Converter.vue";

  const tabs = [
    { label: "Alarm", icon: "pi pi-bell", component: Alarm },
    { label: "Clock", icon: "pi pi-clock", component: Clock },
    { label: "Converter", icon: "pi pi-arrow-right-arrow-left", component: Converter },
  ];
</script>

<style scoped>
  /* Add your styles here */
</style>
