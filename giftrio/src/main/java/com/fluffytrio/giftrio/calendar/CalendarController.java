package com.fluffytrio.giftrio.calendar;

import com.fluffytrio.giftrio.calendar.dto.CalendarRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping()
    public Calendar addCalendar(@RequestBody CalendarRequestDto calendarRequestDto) {
        return calendarService.addCalendar(calendarRequestDto);
    }

    @GetMapping("/{calendarId}")
    public Calendar getCalendar(@PathVariable Long calendarId) {
        return calendarService.getCalendar(calendarId);
    }

    @GetMapping()
    public List<Calendar> getCalendars() {
        return calendarService.getCalendars();
    }

    @PutMapping()
    public Calendar updateCalendar(@RequestBody CalendarRequestDto calendarRequestDto) {
        return calendarService.updateCalendar(calendarRequestDto.toEntity());
    }

    @DeleteMapping("/{calendarId}")
    public boolean deleteCalendar(@PathVariable Long calendarId) {
        return calendarService.deleteCalendar(calendarId);
    }

    @PutMapping("/{calendarId}/password")
    public Calendar updatePassword(@PathVariable Long calendarId, @RequestBody CalendarRequestDto calendarRequestDto) throws NoSuchAlgorithmException {
        return calendarService.updatePassword(calendarRequestDto.toEntity());
    }
}
