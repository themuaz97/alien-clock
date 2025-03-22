<template>
  <div class="relative w-full" ref="datepickerWrapper">
    <div class="flex items-center relative">
      <!-- Icon (conditionally rendered) -->
      <span
        v-if="icon"
        :class="[
          'absolute text-gray-400 cursor-pointer',
          iconPosition === 'start' ? 'left-3' : 'right-3',
        ]"
        @click="toggleDatepicker">
        <i :class="icon"></i>
      </span>

      <!-- Input field -->
      <input
        type="text"
        :class="[
          'px-4 py-2 border bg-gray-700 rounded-lg shadow-sm focus:outline-none focus:border-purple-600 text-ellipsis overflow-hidden whitespace-nowrap',
        ]"
        :placeholder="placeholder"
        v-model="formattedDate"
        @focus="toggleDatepicker"
        readonly />
    </div>

    <!-- Datepicker dropdown -->
    <div
      v-if="isOpen"
      class="datepicker-dropdown absolute z-10 w-full min-w-[280px] max-w-[480px] p-4 mt-2 bg-gray-700 border rounded-lg shadow-lg">
      <!-- Render Time Picker Only -->
      <div v-if="props.timeOnly" class="time-picker">
        <div class="flex items-center justify-center space-x-4">
          <!-- Hours -->
          <div class="flex flex-col items-center">
            <button
              class="text-gray-400 hover:text-white"
              @click="incrementTime('hours')">
              <i class="pi pi-angle-up" />
            </button>
            <span class="font-medium text-lg">{{ formatNumber(hours) }}</span>
            <button
              class="text-gray-400 hover:text-white"
              @click="decrementTime('hours')">
              <i class="pi pi-angle-down" />
            </button>
          </div>
          <div>:</div>

          <!-- Minutes -->
          <div class="flex flex-col items-center">
            <button
              class="text-gray-400 hover:text-white"
              @click="incrementTime('minutes')">
              <i class="pi pi-angle-up" />
            </button>
            <span class="font-medium text-lg">{{ formatNumber(minutes) }}</span>
            <button
              class="text-gray-400 hover:text-white"
              @click="decrementTime('minutes')">
              <i class="pi pi-angle-down" />
            </button>
          </div>
          <div>:</div>

          <!-- am/pm -->
          <div class="flex flex-col items-center">
            <button class="text-gray-400 hover:text-white" @click="toggleAmPm">
              <i class="pi pi-angle-up" />
            </button>
            <span class="font-medium text-lg">{{ amPm }}</span>
            <button class="text-gray-400 hover:text-white" @click="toggleAmPm">
              <i class="pi pi-angle-down" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount } from "vue";

const props = defineProps({
  modelValue: [Date, Object, null],
  placeholder: String,
  selectionMode: {
    type: String,
    default: "single",
  },
  icon: String,
  iconPosition: {
    type: String,
    default: "end",
  },
  showTime: Boolean,
  hourFormat: {
    type: Number,
    default: 12,
  },
  timeOnly: Boolean,
  monthOnly: Boolean,
});

const emit = defineEmits(["update:modelValue"]);

// State
const isOpen = ref(false);
const hours = ref(12);
const minutes = ref(0);
const amPm = ref("am");
const datepickerWrapper = ref(null);

// Initialize time values from modelValue
if (props.timeOnly && props.modelValue instanceof Date) {
  const date = props.modelValue;
  hours.value = date.getHours() % 12 || 12; // Convert to 12-hour format
  minutes.value = date.getMinutes();
  amPm.value = date.getHours() >= 12 ? "pm" : "am";
}

// Watch for changes to hours, minutes, and amPm
watch([hours, minutes, amPm], () => {
  if (props.timeOnly) {
    const hours24 = amPm.value === "am" ? hours.value % 12 : (hours.value % 12) + 12;
    const date = new Date();
    date.setHours(hours24, minutes.value, 0, 0); // Set the time
    emit("update:modelValue", date);
  }
});

// Format time with leading zeros
function formatNumber(num) {
  return num.toString().padStart(2, "0");
}

// Increment and decrement time
function incrementTime(type) {
  if (type === "hours") {
    hours.value = hours.value === 12 ? 1 : hours.value + 1;
  } else if (type === "minutes") {
    minutes.value = (minutes.value + 1) % 60;
  }
}

function decrementTime(type) {
  if (type === "hours") {
    hours.value = hours.value === 1 ? 12 : hours.value - 1;
  } else if (type === "minutes") {
    minutes.value = (minutes.value - 1 + 60) % 60;
  }
}

function toggleAmPm() {
  amPm.value = amPm.value === "am" ? "pm" : "am";
}

// Formatted date for display
const formattedDate = computed(() => {
  if (props.timeOnly) {
    return `${formatNumber(hours.value)}:${formatNumber(minutes.value)} ${amPm.value}`;
  }
  return "";
});

// Toggle datepicker
function toggleDatepicker() {
  isOpen.value = !isOpen.value;
}

// Close datepicker when clicking outside
function handleClickOutside(event) {
  const wrapper = datepickerWrapper.value;
  if (wrapper && !wrapper.contains(event.target)) {
    isOpen.value = false;
  }
}

// Mount and unmount event listener
onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped></style>