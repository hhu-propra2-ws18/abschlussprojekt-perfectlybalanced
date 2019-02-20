package de.hhu.abschlussprojektverleihplattform.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PasswordGenerator {

    public static void main(String[] args) {

        Scanner input = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println("Bitte gebe ein Passwort ein, welches verschlüsselt werden soll:");
        String password = input.nextLine();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        int i = 0;
        while (i < 5) {
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(hashedPassword);
            i++;
        }
    }
}
