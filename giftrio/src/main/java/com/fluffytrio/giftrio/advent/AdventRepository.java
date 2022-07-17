package com.fluffytrio.giftrio.advent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fluffytrio.giftrio.calendar.Calendar;

public interface AdventRepository extends JpaRepository<Advent, Long> {

    List<Advent> findByCalendarId(Calendar calendarId);
}
