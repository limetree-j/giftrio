package com.fluffytrio.giftrio.calendar;

import com.fluffytrio.giftrio.calendar.dto.CalendarRequestDto;
import com.fluffytrio.giftrio.settings.Settings;
import com.fluffytrio.giftrio.settings.SettingsRepository;
import com.fluffytrio.giftrio.user.User;
import com.fluffytrio.giftrio.user.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SettingsRepository settingsRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    private final String CALENDAR_URL = "/api/v1/calendars";

    @After
    public void tearDown() throws Exception {
        calendarRepository.deleteAll();
        settingsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void addCalendarTest() throws Exception {
        User users = User.builder()
                .email("tester")
                .userName("name")
                .password("123456")
                .build();
        users = userRepository.save(users);

        Settings setting = new Settings();
        setting = settingsRepository.save(setting);

        String title = "title";
        String detail = "detail";
        LocalDate startDate = LocalDate.of(2022, 05, 01);
        LocalDate endDate = LocalDate.of(2022, 05, 31);

        CalendarRequestDto calendarDto = CalendarRequestDto.builder()
                .userId(users)
                .settingId(setting)
                .title(title)
                .detail(detail)
                .startDate(startDate)
                .endDate(endDate)
                .backgroundImg("test")
                .build();

        String url = "http://localhost:" + port + CALENDAR_URL;
        //when
        ResponseEntity<Calendar> responseEntity = restTemplate.postForEntity(url, calendarDto, Calendar.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Calendar> all = calendarRepository.findAll();
        assertAll(
                () -> assertThat(all.get(0).getTitle()).isEqualTo(title),
                () -> assertThat(all.get(0).getDetail()).isEqualTo(detail)
        );
    }

    @Test
    public void getCalendarTest() throws Exception {
        //given
        addCalendarTest();
        String url = "http://localhost:" + port + CALENDAR_URL + "/1";

        //when
        ResponseEntity<Calendar> responseEntity = restTemplate.getForEntity(url, Calendar.class);

        //then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody().getUser().getEmail()).isEqualTo("tester")
        );
    }

    @Test
    public void getCalendarsTest() throws Exception {
        //given
        addCalendarTest();
        String url = "http://localhost:" + port + CALENDAR_URL;

        //when
        ResponseEntity<Calendar[]> responseEntity = restTemplate.getForEntity(url, Calendar[].class);
        List<Calendar> calendars = Arrays.asList(responseEntity.getBody());

        //then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(calendars.get(0).getTitle()).isEqualTo("title"),
                () -> assertThat(calendars).hasSize(1)
        );
    }

    @Test
    public void updateCalendarTest() throws Exception {
        //given
        addCalendarTest();
        Optional<User> user = userRepository.findById(1L);
        Optional<Settings> settingId = settingsRepository.findById(1L);

        LocalDate startDate = LocalDate.of(2022, 05, 01);
        LocalDate endDate = LocalDate.of(2022, 05, 31);

        CalendarRequestDto calendarDto = CalendarRequestDto.builder()
                .id(1L)
                .userId(user.get())
                .settingId(settingId.get())
                .title("title2")
                .detail("detail2")
                .startDate(startDate)
                .endDate(endDate)
                .backgroundImg("test")
                .build();

        //when
        String url = "http://localhost:" + port + CALENDAR_URL;
        restTemplate.put(url, calendarDto, Calendar.class);

        //then
        Optional<Calendar> calendar = calendarRepository.findById(1L);
        assertAll(
                () -> assertThat(calendar.get().getTitle()).isEqualTo("title2"),
                () -> assertThat(calendar.get().getDetail()).isEqualTo("detail2")
        );
    }

    @Test
    public void deleteCalendarTest() throws Exception {
        //given
        addCalendarTest();

        //when
        String url = CALENDAR_URL + "/1";
        restTemplate.delete(url);
        Optional<Calendar> calendar = calendarRepository.findById(1L);

        //then
        assertThat(calendar.get().isDelete()).isTrue();
    }

    @Test
    public void updatePasswordTest() throws Exception {
        //given
        addCalendarTest();
        Optional<User> user = userRepository.findById(1L);
        Optional<Settings> settingId = settingsRepository.findById(1L);

        LocalDate startDate = LocalDate.of(2022, 05, 01);
        LocalDate endDate = LocalDate.of(2022, 05, 31);

        CalendarRequestDto calendarDto = CalendarRequestDto.builder()
                .id(1L)
                .userId(user.get())
                .settingId(settingId.get())
                .title("title")
                .detail("detail")
                .startDate(startDate)
                .endDate(endDate)
                .backgroundImg("test")
                .password("password")
                .build();

        //when
        String url = "http://localhost:" + port + CALENDAR_URL + "/1/password";
        restTemplate.put(url, calendarDto, Calendar.class);

        //then
        Optional<Calendar> calendar = calendarRepository.findById(1L);
        assertAll(
                () -> assertThat(calendar.get().getPassword()).isNotNull()
        );
    }
}
