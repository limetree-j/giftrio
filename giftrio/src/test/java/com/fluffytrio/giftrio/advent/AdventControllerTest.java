package com.fluffytrio.giftrio.advent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffytrio.giftrio.advent.dto.AdventRequestDto;
import com.fluffytrio.giftrio.calendar.Calendar;
import com.fluffytrio.giftrio.calendar.CalendarRepository;
import com.fluffytrio.giftrio.settings.Settings;
import com.fluffytrio.giftrio.settings.SettingsRepository;
import com.fluffytrio.giftrio.user.User;
import com.fluffytrio.giftrio.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdventControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdventRepository adventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    public String getApiUrl(){
        return String.format("http://localhost:%d/api/v1/advents", port);
    }

    @After
    public void tearDown() throws Exception {
        adventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void addAdvent() throws Exception {
        //given
        int seqNum = 1;
        LocalDate adventDate = LocalDate.now();
        String text = "advent create test";
        String img = "http://img_url.png";

        // create user
        String userId = "user01";
        String userName = "nickname";
        String password = "password";
        userRepository.save(User.builder().email(userId).userName(userName).password(password).build());
        User users1 = userRepository.findAll().get(0);

        // create setting
        settingsRepository.save(new Settings());
        Settings settings1 =  settingsRepository.findAll().get(0);

        // create calendar
        calendarRepository.save(Calendar.builder().user(users1).settingId(settings1).build());
        Calendar calendar1 = calendarRepository.findAll().get(0);

        AdventRequestDto adventRequestDto = AdventRequestDto.builder()
                                                .userId(users1)
                                                .calendarId(calendar1)
                                                .seqNum(seqNum)
                                                .adventDate(adventDate)
                                                .text(text)
                                                .img(img)
                                                .isOpen(false)
                                                .build();

        //when
        ResponseEntity<Advent> responseEntity = restTemplate.postForEntity(getApiUrl(), adventRequestDto, Advent.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Advent> adventList = adventRepository.findAll();
        assertAll(
            () -> assertThat(adventList.get(0).getAdventDate()).isEqualTo(adventDate),
            () -> assertThat(adventList.get(0).getText()).isEqualTo(text),
            () -> assertThat(adventList.get(0).getImg()).isEqualTo(img)
        );
    }

    @Test
    @Transactional
    public void getAdvent() throws Exception {
        //given
        addAdvent();
        long adventId = 14;

        //when
        ResponseEntity<Advent> responseEntity = restTemplate.getForEntity(String.format("%s/%d", getApiUrl(), adventId), Advent.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getId()).isEqualTo(adventId);
    }

    @Test
    @Transactional
    public void getAdvents() throws Exception {
        //when
        ResponseEntity<Advent[]> responseEntity = restTemplate.getForEntity(getApiUrl(), Advent[].class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Advent> advents = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        assertThat(advents.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void updateAdvent() throws Exception {
        //given
        addAdvent();
        long adventId = 14;

        int seqNum = 3;
        String text = "Update test";
        AdventRequestDto adventRequestDto = AdventRequestDto.builder()
                .id(adventId)
                .seqNum(seqNum)
                .text(text)
                .build();

        //when
        restTemplate.put(String.format("%s/%d", getApiUrl(), adventId), adventRequestDto, Advent.class);

        //then
        Optional<Advent> advent = adventRepository.findById(adventId);
        assertAll(
            () -> assertThat(advent.get().getId()).isEqualTo(adventId),
            () ->  assertThat(advent.get().getText()).isEqualTo(text),
            () -> assertThat(advent.get().getSeqNum()).isEqualTo(seqNum)
        );
    }

    @Test
    @Transactional
    public void deleteAdvent() throws Exception {
        //given
        addAdvent();
        long adventId = 14;

        //when
        restTemplate.delete(String.format("%s/%d", getApiUrl(), adventId), Boolean.class);

        //then
        Optional<Advent> advent = adventRepository.findById(adventId);
        advent.ifPresent(value -> assertThat(value.isDelete()).isEqualTo(true));
    }

    @Test
    @Transactional
    public void getAdventsByCalendarId() throws Exception {
        //given
        long calendarId = 17;

        //when
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(
            String.format("%s/calendars/%d", getApiUrl(), calendarId),Boolean.class
        );

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(true);

    }

}
