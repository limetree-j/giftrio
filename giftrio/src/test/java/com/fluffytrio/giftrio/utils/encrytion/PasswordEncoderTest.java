package com.fluffytrio.giftrio.utils.encrytion;

import com.fluffytrio.giftrio.utils.encryption.PasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PasswordEncoderTest {

    @Test
    public void createDelegatingPasswordEncoderTest() throws NoSuchAlgorithmException {
        String password = "password";
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);

        assertThat(passwordEncoder.matches(password, encodePassword)).isTrue();
    }
}
