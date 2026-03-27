package com.ayur;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AyurvedaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AyurvedaApplication.class, args);
    }

    // This automatically creates a test user if your database is empty!
    @Bean
    public CommandLineRunner seedDatabase(ClientRepository clientRepository) {
        return args -> {
            if (clientRepository.count() == 0) {
                Client testPatient = new Client();
                testPatient.setName("Aarav Sharma");
                testPatient.setUsername("patient");
                testPatient.setPassword("password123");
                testPatient.setRole("PATIENT");
                testPatient.setPrimaryDosha("Pitta");
                
                clientRepository.save(testPatient);
                System.out.println("Test patient created! Username: patient | Password: password123");
            }
        };
    }
}