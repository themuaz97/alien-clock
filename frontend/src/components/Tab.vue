<script setup>
import { ref, computed, watch } from "vue";

const props = defineProps({
  tabs: {
    type: Array,
    required: true,
  },
  activeTab: {
    type: Number,
    default: 0,
  },
});

const activeIndex = ref(props.activeTab);

// Compute indicator position dynamically
const indicatorStyle = computed(() => ({
  left: `${activeIndex.value * 130 + 2}px`,
}));

// Dynamically load the current tab component
const activeComponent = computed(() => props.tabs[activeIndex.value]?.component);

// Watch for any changes in the activeTab prop (if needed)
watch(() => props.activeTab, (newIndex) => {
  activeIndex.value = newIndex;
});
</script>

<template>
  <div class="tab-container">
    <template v-for="(tab, index) in tabs" :key="index">
      <input
        type="radio"
        :id="'tab' + index"
        name="tab"
        class="tab"
        :checked="activeIndex === index"
        @change="activeIndex = index"
      />
      <label class="tab_label" :for="'tab' + index">
        <i :class="tab.icon" v-if="tab.icon"></i>
        {{ tab.label }}
      </label>
    </template>

    <div class="indicator" :style="indicatorStyle"></div>
  </div>

  <!-- Render the active component -->
  <div class="mt-2 p-4">
    <component :is="activeComponent" />
  </div>
</template>

<style scoped>
.tab-container {
  position: relative;
  display: flex;
  align-items: flex-start;
  padding: 2px;
  background-color: rgb(24 24 27 / var(--tw-bg-opacity, 1));
  border-radius: 9px;
}

.indicator {
  width: 130px;
  height: 28px;
  background-color: rgb(63 63 70 / var(--tw-bg-opacity, 1));
  position: absolute;
  top: 2px;
  left: 2px;
  z-index: 9;
  border: 0.5px solid rgba(0, 0, 0, 0.04);
  box-shadow: 0px 3px 8px rgba(0, 0, 0, 0.12), 0px 3px 1px rgba(0, 0, 0, 0.04);
  border-radius: 7px;
  transition: all 0.2s ease-out;
}

.tab {
  width: 130px;
  height: 28px;
  position: absolute;
  z-index: 99;
  outline: none;
  opacity: 0;
}

.tab_label {
  width: 130px;
  height: 28px;
  position: relative;
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 0;
  font-size: 0.75rem;
  opacity: 0.6;
  cursor: pointer;
  gap: 5px;
}
</style>
