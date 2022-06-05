package com.fluffytrio.giftrio.advent;

import com.fluffytrio.giftrio.advent.dto.AdventRequestDto;
import com.fluffytrio.giftrio.calendar.Calendar;
import com.fluffytrio.giftrio.calendar.CalendarRepository;
import com.fluffytrio.giftrio.settings.Settings;
import com.fluffytrio.giftrio.settings.SettingsRepository;
import com.fluffytrio.giftrio.users.Users;
import com.fluffytrio.giftrio.users.UsersRepository;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    private UsersRepository usersRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @After
    public void tearDown() throws Exception {
        adventRepository.deleteAll();
    }

    @Test
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
        usersRepository.save(Users.builder().userId(userId).userName(userName).password(password).build());
        Users users1 = usersRepository.findAll().get(0);

        // create setting
        settingsRepository.save(new Settings());
        Settings settings1 =  settingsRepository.findAll().get(0);

        // create calendar
        calendarRepository.save(Calendar.builder().users(users1).settings(settings1).build());
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

        String url = "http://localhost:" + port + "/api/v1/advent";

        //when
        ResponseEntity<Advent> responseEntity = restTemplate.postForEntity(url, adventRequestDto, Advent.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Advent> adventList = adventRepository.findAll();
        assertThat(adventList.get(0).getAdventDate()).isEqualTo(adventDate);
        assertThat(adventList.get(0).getText()).isEqualTo(text);
        assertThat(adventList.get(0).getImg()).isEqualTo(img);
    }
}