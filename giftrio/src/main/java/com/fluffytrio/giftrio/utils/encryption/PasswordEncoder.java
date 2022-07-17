package com.fluffytrio.giftrio.utils.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    public String encode(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for(byte b : md.digest()) {
            sb.append(b);
        }

        return sb.toString();
    }

    public boolean matches(String password, String encodePassword) throws NoSuchAlgorithmException {
        password = this.encode(password);
        return password.equals(encodePassword);
    }
}
