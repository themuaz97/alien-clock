package com.srkay.alien_clock.repository;

import com.srkay.alien_clock.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
