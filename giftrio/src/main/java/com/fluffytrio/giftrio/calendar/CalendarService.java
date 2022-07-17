package com.fluffytrio.giftrio.calendar;

import com.fluffytrio.giftrio.calendar.dto.CalendarRequestDto;
import com.fluffytrio.giftrio.utils.encryption.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Transactional
    public Calendar addCalendar(CalendarRequestDto calendarRequestDto) {
        return calendarRepository.save(calendarRequestDto.toEntity());
    }

    public Calendar getCalendar(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캘린더 입니다."));
    }

    public List<Calendar> getCalendars() {
        return calendarRepository.findAll();
    }

    @Transactional
    public Calendar updateCalendar(Calendar update) {
        this.getCalendar(update.getId());
        return calendarRepository.save(update);
    }

    @Transactional
    public boolean deleteCalendar(Long id) {
        Calendar calendar = this.getCalendar(id);
        calendar.delete();
        return calendarRepository.save(calendar).isDelete();
    }

    @Transactional
    public Calendar updatePassword(Calendar updateCalendar) throws NoSuchAlgorithmException {
        String password = updateCalendar.getPassword();
        if (password == null && password == "") {
            throw new IllegalArgumentException("잘못된 값을 입력하였습니다.");
        }
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        password = passwordEncoder.encode(password);

        Calendar calendar = this.getCalendar(updateCalendar.getId());
        calendar.changePassword(password);
        return calendarRepository.save(calendar);
    }
}
