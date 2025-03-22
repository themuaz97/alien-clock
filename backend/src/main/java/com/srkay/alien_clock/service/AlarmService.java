package com.srkay.alien_clock.service;

import com.srkay.alien_clock.entity.Alarm;
import com.srkay.alien_clock.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public List<Alarm> getAlarms() {
        return alarmRepository.findAll();
    }

    public Alarm getAlarmById(Long id) {
        return alarmRepository.findById(id).orElse(null);
    }

    public Alarm addAlarm(Alarm alarm) {
        if (alarm.getTitle() == null || alarm.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (alarm.getAlarm() == null) {
            throw new IllegalArgumentException("Alarm is required");
        }

        // Ensure the alarm has a valid time zone (set to Malaysia Time if missing)
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
        if (alarm.getAlarm().getOffset() == null) {
            alarm.setAlarm(alarm.getAlarm().atZoneSameInstant(malaysiaZone).toOffsetDateTime());
        }

        // Set default values
        if (alarm.getIsActive() == null) {
            alarm.setIsActive(true); // Default isActive to true
        }
        if (alarm.getRingtone() == null || alarm.getRingtone().isBlank()) {
            alarm.setRingtone("/audio/ringtones/ringtone.mp3"); // Set default ringtone path
        }

        return alarmRepository.save(alarm);
    }

    public Alarm updateAlarm(Long id, Alarm alarm) {
        Alarm existingAlarm = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alarm not found with ID: " + id));

        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");

        // Update only the necessary fields
        if (alarm.getTitle() != null) {
            existingAlarm.setTitle(alarm.getTitle());
        }

        if (alarm.getAlarm() != null) {
            // Ensure the alarm time is set to Malaysia Time if no time zone is present
            OffsetDateTime offsetDateTime = alarm.getAlarm().atZoneSameInstant(malaysiaZone).toOffsetDateTime();
            existingAlarm.setAlarm(offsetDateTime);
        }

        if (alarm.getRingtone() != null) {
            existingAlarm.setRingtone(alarm.getRingtone());
        } else {
            // Keep the existing ringtone if none is provided
            alarm.setRingtone(existingAlarm.getRingtone());
        }

        if (alarm.getDescription() != null) {
            existingAlarm.setDescription(alarm.getDescription());
        }

        if (alarm.getIsActive() != null) {
            existingAlarm.setIsActive(alarm.getIsActive());
        }

        return alarmRepository.save(existingAlarm);
    }

    public Alarm switchAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alarm not found with id" + id));
        alarm.setIsActive(!alarm.getIsActive()); // Toggle the isActive state
        return alarmRepository.save(alarm);
    }
}
