package de.hhu.abschlussprojektverleihplattform.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

public class PasswordGenerator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in, "UTF-8");
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
