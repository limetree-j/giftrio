package com.fluffytrio.giftrio.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffytrio.giftrio.user.dto.UserRequestDto;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void tearDown() throws Exception {
        //userRepository.deleteAll();
    }

    @Test
    public void addUser() throws Exception {
        //given
        String role = "USER";
        String email = "email@gmail.com";
        String userName = "userName";
        String password = "password";

        UserRequestDto requestDto = UserRequestDto
                .builder()
                .role(Role.valueOf(role))
                .email(email)
                .userName(userName)
                .password(password)
                .build();

        String url = "http://localhost:"+port+"/api/v1/users";

        //when
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, requestDto, User.class);

        //then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody().getRole()).isEqualTo(Role.USER),
                () -> assertThat(responseEntity.getBody().getEmail()).isEqualTo(email)
        );
    }

    @Test
    public void getUser() throws Exception {
        //given
        String url = "http://localhost:"+port+"/api/v1/users/1";

        //when
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);

        //then
        String email = "email@gmail.com";
        String userName = "userName";
        String password = "password";



        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody().getEmail()).isEqualTo(email),
                () -> assertThat(responseEntity.getBody().getUserName()).isEqualTo(userName),
                () -> assertThat(responseEntity.getBody().getPassword()).isEqualTo(password)
        );
    }

    @Test
    public void getUsers() throws Exception {
        //given
        String url = "http://localhost:"+port+"/api/v1/users";

        //when
        ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(url, User[].class);

        //then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(responseEntity.getBody().length).isEqualTo(1)
        );
    }

    @Test
    public void updateUsers() throws Exception {
        //given
        String url = "http://localhost:"+port+"/api/v1/users/1";

        String email = "email@gmail.com";
        String userName = "userName2";
        String password = "password2";

        User requestDto = UserRequestDto
                .builder()
                .id(1L)
                .email(email)
                .userName(userName)
                .password(password)
                .build().toEntity();

        //when
        restTemplate.put(url, requestDto, User.class);

        //then
        User responseEntity = userRepository.findById(1L).get();
        assertAll(
                () -> assertThat(responseEntity.getEmail()).isEqualTo(email),
                () -> assertThat(responseEntity.getUserName()).isEqualTo(userName),
                () -> assertThat(responseEntity.getPassword()).isEqualTo(password)
        );
    }

    @Test
    public void deleteUsers() throws Exception {
        //given
        String url = "http://localhost:"+port+"/api/v1/users/1";

        //when
        restTemplate.delete(url);

        //then
        User responseEntity = userRepository.findById(1L).get();
        assertThat(responseEntity.isDelete() == true);
    }
}
