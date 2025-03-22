package com.srkay.alien_clock.controller;

import com.srkay.alien_clock.entity.Alarm;
import com.srkay.alien_clock.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<List<Alarm>> getAlarms() {
        List<Alarm> alarms = alarmService.getAlarms();
        return new ResponseEntity<>(alarms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alarm> getAlarmById(@PathVariable Long id) {
        Alarm alarms = alarmService.getAlarmById(id);
        return new ResponseEntity<>(alarms, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Alarm> addAlarm(@RequestBody Alarm alarm) {
        Alarm newAlarm = alarmService.addAlarm(alarm);
        return new ResponseEntity<>(newAlarm, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Alarm> updateAlarm(@PathVariable Long id, @RequestBody Alarm alarm) {
        Alarm updatedAlarm = alarmService.updateAlarm(id, alarm);
        return new ResponseEntity<>(updatedAlarm, HttpStatus.OK);
    }

    @PutMapping("/switch/{id}")
    public ResponseEntity<Alarm> switchAlarm(@PathVariable Long id) {
        Alarm switchAlarm = alarmService.switchAlarm(id);
        return new ResponseEntity<>(switchAlarm, HttpStatus.OK);
    }
}
