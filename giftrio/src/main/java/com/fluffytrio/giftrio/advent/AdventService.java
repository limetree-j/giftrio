package com.fluffytrio.giftrio.advent;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fluffytrio.giftrio.advent.dto.AdventRequestDto;
import com.fluffytrio.giftrio.calendar.Calendar;
import com.fluffytrio.giftrio.calendar.CalendarRepository;

@RequiredArgsConstructor
@Service
public class AdventService {

    private final AdventRepository adventRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public Advent addAdvent(AdventRequestDto adventRequestDto){
        return adventRepository.save(adventRequestDto.toEntity());
    }

    @Transactional
    public Optional<Advent> getAdvent(Long adventId){
        if(adventRepository.findById(adventId).isEmpty()){
            throw new IllegalArgumentException("ID값이 없습니다.");
        }
        return adventRepository.findById(adventId);
    }

    @Transactional
    public List<Advent> getAdvents(){
        return adventRepository.findAll();
    }

    @Transactional
    public Advent updateAdvent(AdventRequestDto adventRequestDto){
        Optional<Advent> originAdvent = adventRepository.findById(adventRequestDto.toEntity().getId());
        if(originAdvent.isEmpty()){
            throw new IllegalArgumentException("ID값이 없습니다.");
        }
        return adventRepository.save(adventRequestDto.toEntity());
    }

    @Transactional
    public boolean deleteAdvent(Long adventId){
        Optional<Advent> advent = adventRepository.findById(adventId);
        if(advent.isPresent()){
            advent.get().delete();
            return adventRepository.save(advent.get()).isDelete();
        }
        return false;
    }

    public boolean getAdventByCalendarId(Long calendarId){
        Optional<Calendar> calendar = calendarRepository.findById(calendarId);
        if(calendar.isEmpty()) {
            throw new IllegalArgumentException("ID값이 없습니다.");
        }
        List<Advent> advents = adventRepository.findByCalendarId(calendar.get());
        return ! advents.isEmpty();
    }
}
