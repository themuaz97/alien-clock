<!-- @format -->

<template>
  <div class="flex flex-col items-center gap-4">
    <div
      class="flex flex-col justify-center items-center gap-4"
      data-alien-time-subscriber>
      <div>
        <h2 class="flex gap-2 text-7xl font-bold">
          <span>{{ alienTimeStore.earthTimeParts[0] }}</span> :
          <span>{{ alienTimeStore.earthTimeParts[1] }}</span> :
          <span>{{ alienTimeStore.earthTimeParts[2] }}</span>
        </h2>
      </div>
      <span
        >{{ alienTimeStore.earthFormattedDate }} -
        {{ alienTimeStore.earthTimeZone }}</span
      >
    </div>

    <div class="flex justify-end w-full gap-2 mt-12">
      <Button v-if="isAlarmRinging" @click="stopAlarm" icon="pi pi-stop" />
      <audio ref="audioPlayer"></audio>

      <Button @click="addModal = true" icon="pi pi-plus" size="small"></Button>
      <Modal v-model:isOpen="addModal" header="Add Alarm">
        <form @submit.prevent>
          <div class="flex flex-col gap-4">
            <div class="flex flex-col gap-2">
              <label class="block text-sm font-medium">Title</label>
              <input
                v-model="alarm.title"
                type="text"
                class="w-full p-2 border rounded-lg bg-gray-700" />
              <small v-if="alarmError?.detail?.title" class="text-red-500">{{
                alarmError?.detail?.title
              }}</small>
            </div>

            <div class="flex flex-col gap-2">
              <label class="block text-sm font-medium">Description</label>
              <textarea
                v-model="alarm.description"
                class="w-full p-2 border rounded-lg bg-gray-700"></textarea>
            </div>

            <div class="flex flex-col gap-2 mb-4">
              <label class="block text-sm font-medium">Alarm Time</label>
              <Datepicker
                v-model="alarm.alarm"
                :timeOnly="true"
                style="width: 100px" />
              <small v-if="alarmError.detail.alarm" class="text-red-500">{{
                alarmError.detail.alarm
              }}</small>
            </div>
          </div>

          <Button type="submit" @click="addAlarm" label="Save"></Button>
        </form>
      </Modal>
    </div>

    <div v-if="alarms.length > 0">
      <Card
        v-for="(alarm, index) in alarms"
        :key="index"
        class="card"
        @click="viewAlarm(alarm.id)">
        <div class="flex justify-between items-center gap-28">
          <div class="flex flex-col justify-center gap-2">
            <h2 class="text-2xl font-bold">
              {{ alarm.title }}
            </h2>
            <span class="text-xl">{{ formatTime(alarm.alarm) }}</span>
            <span>{{ formatDay(alarm.alarm) }}</span>
          </div>
          <Switch
            :checked="alarm.isActive"
            @update:checked="(value) => switchAlarm(alarm.id, value)"
            @click.stop />
        </div>
      </Card>
    </div>
    <div v-else>
      <p>No alarms found.</p>
    </div>

    <!-- View Alarm Modal -->
    <Modal v-model:isOpen="viewModal" header="View Alarm">
      <div v-if="selectedAlarm" class="flex flex-col gap-4">
        <div class="flex flex-col gap-2">
          <label class="block text-sm font-medium">Title</label>
          <input
            v-model="selectedAlarm.title"
            type="text"
            class="w-full p-2 border rounded-lg bg-gray-700" />
        </div>

        <div class="flex flex-col gap-2">
          <label class="block text-sm font-medium">Description</label>
          <textarea
            v-model="selectedAlarm.description"
            class="w-full p-2 border rounded-lg bg-gray-700"></textarea>
        </div>

        <div class="flex flex-col gap-2 mb-4">
          <label class="block text-sm font-medium">Alarm Time</label>
          <Datepicker
            v-model="selectedAlarm.alarm"
            :timeOnly="true"
            style="width: 100px" />
        </div>
        <Button type="submit" @click="updateAlarm" label="Save"></Button>
      </div>
    </Modal>
  </div>
</template>

<script setup>
  import Datepicker from "@/components/Datepicker.vue";
  import Card from "@/components/Card.vue";
  import Button from "@/components/Button.vue";
  import Switch from "@/components/Switch.vue";
  import Modal from "@/components/Modal.vue";
  import { onMounted, onUnmounted, ref, watch } from "vue";
  import { useAlienTimeStore } from "@/stores/alienTime";

  const alienTimeStore = useAlienTimeStore();

  const alarms = ref([]);
  const addModal = ref(false);
  const viewModal = ref(false);
  const alarm = ref({
    title: "",
    description: "",
    alarm: null,
  });
  const selectedAlarm = ref(null);
  const alarmError = ref({
    message: "",
    detail: {
      title: "",
      alarm: "",
    },
  });

  const audioPlayer = ref(null);
  const currentRingtone = ref("");
  const isAlarmRinging = ref(false);

  const localTime = new Date(); // Local time
  const utcTime = localTime.toISOString(); // Convert to UTC
  alarm.value.alarm = utcTime;

  const fetchAlarms = async () => {
    const res = await fetch("/api/v1/alarms");

    if (res.ok) {
      const data = await res.json();
      alarms.value = data;
    } else {
      console.error("Failed to fetch alarms");
    }
  };

  const switchAlarm = async (id, isActive) => {
    try {
      const res = await fetch(`/api/v1/alarms/switch/${id}`, {
        method: "PUT",
        body: JSON.stringify({ isActive }),
      });

      if (res.ok) {
        const switchedAlarm = await res.json();
        const index = alarms.value.findIndex((alarm) => alarm.id === id);
        if (index !== -1) {
          alarms.value[index] = switchedAlarm;
        }
      } else {
        console.error("Failed to switch alarm");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const addAlarm = async () => {
    try {
      const res = await fetch("/api/v1/alarms", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(alarm.value),
      });

      const data = await res.json();

      if (res.ok) {
        alarms.value.push(data);
        addModal.value = false;
        alarm.value = {
          title: "",
          description: "",
          alarm: null,
        };
        alarmError.value = {
          message: "",
          detail: {
            title: "",
            alarm: "",
          },
        };
      } else {
        alarmError.value = data;
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const viewAlarm = async (id) => {
    try {
      const res = await fetch(`/api/v1/alarms/${id}`);
      if (res.ok) {
        const data = await res.json();
        // Convert the alarm string to a Date object
        data.alarm = new Date(data.alarm);
        selectedAlarm.value = data;
        viewModal.value = true;
      } else {
        console.error("Failed to fetch alarm details");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const updateAlarm = async () => {
    try {
      const res = await fetch(
        `/api/v1/alarms/update/${selectedAlarm.value.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(selectedAlarm.value), // Send the updated object
        }
      );

      const data = await res.json();

      if (res.ok) {
        viewModal.value = false;
        fetchAlarms();
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const checkAlarms = () => {
    setInterval(() => {
      const now = new Date();

      alarms.value.forEach((alarm) => {
        if (!alarm.isActive) return;

        const alarmTime = new Date(alarm.alarm);

        // Only trigger if current time is after or equal to alarm time
        // and within a small window (1 minute) to catch it
        if (now >= alarmTime && now - alarmTime < 60000 && !alarm.triggered) {
          console.log(`Alarm Triggered: ${alarm.title}`);

          // Get the full path for the ringtone
          const ringtonePath = alarm.ringtone;
          playAlarm(ringtonePath);

          // Mark as triggered to prevent repeated triggering
          alarm.triggered = true;

          // Reset the triggered flag after the alarm duration
          setTimeout(() => {
            alarm.triggered = false;
          }, 120000);
        }
      });
    }, 1000); // Check every second
  };

  const playAlarm = (ringtonePath) => {
    currentRingtone.value = ringtonePath;
    if (audioPlayer.value) {
      // Make sure the path is properly formatted
      audioPlayer.value.src = ringtonePath;
      audioPlayer.value.play().catch((err) => {
        console.error("Failed to play ringtone:", err);
      });
      isAlarmRinging.value = true;

      // Auto-stop after 1 minute
      setTimeout(() => {
        stopAlarm();
      }, 60000);
    }
  };

  const stopAlarm = () => {
    if (audioPlayer.value) {
      audioPlayer.value.pause();
      audioPlayer.value.currentTime = 0; // Reset to start
      isAlarmRinging.value = false; // Reset alarm ringing state
    }
  };

  const formatTime = (dateTime) => {
    const date = new Date(dateTime);
    return date.toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatDay = (dateTime) => {
    const date = new Date(dateTime);
    return date.toLocaleDateString("en-US", { weekday: "long" });
  };

  onMounted(() => {
    alienTimeStore.startTimer();
    fetchAlarms();
    checkAlarms();
  });

  onUnmounted(() => {
    alienTimeStore.cleanupTimer();
  });

  watch(addModal, (isOpen) => {
    if (!isOpen) {
      alarmError.value = {
        message: "",
        detail: {
          title: "",
          alarm: "",
        },
      };
    }
  });
</script>

<style scoped>
  .card {
    cursor: pointer;
    transition: background-color 0.2s ease;
  }

  .card:hover {
    background-color: rgba(255, 255, 255, 0.05);
  }
</style>
