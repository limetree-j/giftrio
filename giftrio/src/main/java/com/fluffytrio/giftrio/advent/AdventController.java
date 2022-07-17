package com.fluffytrio.giftrio.advent;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.fluffytrio.giftrio.advent.dto.AdventRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/advents")
public class AdventController {

    private final AdventService adventService;

    @GetMapping("/{adventId}")
    public Optional<Advent> getAdvent(@PathVariable Long adventId){
        return adventService.getAdvent(adventId);
    }

    @GetMapping()
    public List<Advent> getAdvents(){
        return adventService.getAdvents();
    }

    @PostMapping()
    public boolean addAdvent(@RequestBody AdventRequestDto adventRequestDto){
        return adventService.addAdvent(adventRequestDto);
    }

    @PutMapping("/{adventId}")
    public Advent updateAdvent(@RequestBody AdventRequestDto adventRequestDto){
        return adventService.updateAdvent(adventRequestDto);
    }

    @DeleteMapping("/{adventId}")
    public boolean deleteAdvent(@PathVariable Long adventId){
        return adventService.deleteAdvent(adventId);
    }

    @GetMapping("/calendars/{calendarId}")
    public boolean getAdventByCalendarId(@PathVariable Long calendarId){
        return adventService.getAdventByCalendarId(calendarId);
    }

}
