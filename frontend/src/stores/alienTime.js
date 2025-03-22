/** @format */

// src/stores/alienTimeStore.js
import { defineStore } from "pinia";
import { ref } from "vue";

export const useAlienTimeStore = defineStore("alienTime", {
  state: () => ({
    // Alien time variables
    alienTime: {
      year: 2804,
      month: 18,
      day: 31,
      hour: 2,
      minute: 2,
      second: 88,
    },

    // Earth time variables
    earthTimeParts: ["00", "00", "00"],
    earthFormattedDate: "",
    earthTimeZone: "",

    // Timer reference
    timer: null,

    // Month days lookup table
    monthDays: [
      44, 42, 48, 40, 48, 44, 40, 48, 42, 40, 40, 42, 44, 48, 42, 40, 44, 38,
    ],
  }),

  actions: {
    padZero(num, size = 2) {
      return num.toString().padStart(size, "0");
    },

    calculateAlienTime(earthDate) {
      const earthEpoch = new Date("1970-01-01T00:00:00Z").getTime();
      const earthMillisecondsSinceEpoch = earthDate.getTime() - earthEpoch;
      const earthSecondsSinceEpoch = earthMillisecondsSinceEpoch / 1000;
      const alienSecondsSinceEpoch = earthSecondsSinceEpoch * 2;

      let remainingSeconds = alienSecondsSinceEpoch;

      const alienSecondsPerMinute = 90;
      const alienMinutesPerHour = 90;
      const alienHoursPerDay = 36;

      const alienSecond = Math.floor(remainingSeconds % alienSecondsPerMinute);
      remainingSeconds = Math.floor(remainingSeconds / alienSecondsPerMinute);

      const alienMinute = Math.floor(remainingSeconds % alienMinutesPerHour);
      remainingSeconds = Math.floor(remainingSeconds / alienMinutesPerHour);

      const alienHour = Math.floor(remainingSeconds % alienHoursPerDay);
      let alienDayCount = Math.floor(remainingSeconds / alienHoursPerDay);

      let alienYear = 2804;
      let alienMonth = 18;
      let alienDay = 31;

      alienDay += alienDayCount;

      while (true) {
        const currentMonthDays = this.monthDays[alienMonth - 1];

        if (alienDay <= currentMonthDays) break;

        alienDay -= currentMonthDays;
        alienMonth++;

        if (alienMonth > 18) {
          alienMonth = 1;
          alienYear++;
        }
      }

      return {
        year: alienYear,
        month: alienMonth,
        day: alienDay,
        hour: alienHour,
        minute: alienMinute,
        second: alienSecond,
      };
    },

    updateClock() {
      const now = new Date();

      const timeString = now.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      });

      this.earthTimeParts = timeString.split(":");

      this.earthFormattedDate = now.toLocaleDateString("en-GB", {
        day: "numeric",
        month: "long",
        year: "numeric",
      });

      this.earthTimeZone = Intl.DateTimeFormat()
        .resolvedOptions()
        .timeZone.replace(/_/g, " ");

      this.alienTime = this.calculateAlienTime(now);
    },

    startTimer() {
      if (!this.timer) {
        this.updateClock();
        this.timer = setInterval(this.updateClock, 500);
      }
    },

    stopTimer() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    cleanupTimer() {
      if (this.timer && typeof window !== "undefined") {
        const storeSubscribers = document.querySelectorAll(
          "[data-alien-time-subscriber]"
        );
        if (storeSubscribers.length <= 1) {
          this.stopTimer();
        }
      }
    },
  },
});
