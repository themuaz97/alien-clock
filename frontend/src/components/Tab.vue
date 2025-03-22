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

</style>
