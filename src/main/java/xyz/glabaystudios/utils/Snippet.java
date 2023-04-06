package xyz.glabaystudios.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Snippet {

    public static void main(String[] args) {

        String encryptedPin = new BCryptPasswordEncoder().encode("0905");
        System.out.println(encryptedPin);
    }
}
