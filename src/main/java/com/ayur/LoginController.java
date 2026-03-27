//LoginController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Client loginData) {
        
        // 1. Safely search for the user, IGNORING any broken/empty rows in the database
        Optional<Client> user = clientRepository.findAll().stream()
                .filter(u -> u.getUsername() != null && u.getPassword() != null) // THIS IS THE MAGIC FIX!
                .filter(u -> u.getUsername().equals(loginData.getUsername()) && u.getPassword().equals(loginData.getPassword()))
                .findFirst();

        if (user.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("role", user.get().getRole());
            response.put("username", user.get().getUsername());
            return response;
        }
        
        return Collections.singletonMap("role", "INVALID");
    }
}