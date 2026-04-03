//AyurvedaApplication.java
package com.ayur;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AyurvedaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AyurvedaApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(ClientRepository clientRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("🚀 Checking for seed data...");

            // Seed Patient
            if (clientRepository.findByUsername("patient").isEmpty()) {
                Client testPatient = new Client();
                testPatient.setName("Aarav Sharma");
                testPatient.setUsername("patient");
                testPatient.setPassword(passwordEncoder.encode("password123"));
                testPatient.setRole("PATIENT");
                testPatient.setPrimaryDosha("Pitta");
                testPatient.setAgniStrength("Tikshna");
                testPatient.setPrakriti("Pitta-Kapha");
                clientRepository.save(testPatient);
                System.out.println("✔ PATIENT seeded: patient/password123");
            }

            // Seed Doctor
            if (clientRepository.findByUsername("doctor").isEmpty()) {
                Client testDoctor = new Client();
                testDoctor.setName("Dr. Ayurveda");
                testDoctor.setUsername("doctor");
                testDoctor.setPassword(passwordEncoder.encode("doctor123"));
                testDoctor.setRole("DOCTOR");
                clientRepository.save(testDoctor);
                System.out.println("✔ DOCTOR seeded: doctor/doctor123");
            }

            // Seed Admin
            if (clientRepository.findByUsername("admin").isEmpty()) {
                Client testAdmin = new Client();
                testAdmin.setName("System Admin");
                testAdmin.setUsername("admin");
                testAdmin.setPassword(passwordEncoder.encode("admin123"));
                testAdmin.setRole("ADMIN");
                clientRepository.save(testAdmin);
                System.out.println("✔ ADMIN seeded: admin/admin123");
            }
        };
    }
}