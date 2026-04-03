//LoginController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private ClientRepository clientRepository;

    // ─────────────────────────────────────────────────────────────────
    // PasswordEncoder is the BCryptPasswordEncoder bean defined in
    // SecurityConfig. It exposes matches(rawPassword, storedHash)
    // which verifies a plain-text password against a BCrypt hash
    // without ever decrypting it.
    // ─────────────────────────────────────────────────────────────────
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Client loginData) {

        // ── SECURITY FIX: single DB lookup instead of findAll() ──────
        // Old code: clientRepository.findAll().stream().filter(...)
        //   Problem: loads EVERY user into memory, compares passwords
        //   in plain text, O(n) on every login.
        //
        // New code: one indexed SELECT by username, then BCrypt check.
        //   Fast, safe, and doesn't expose other users' data.
        return clientRepository.findByUsername(loginData.getUsername())
                .filter(user -> user.getPassword() != null
                        && passwordEncoder.matches(loginData.getPassword(), user.getPassword()))
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("role",     user.getRole());
                    response.put("username", user.getUsername());
                    // TODO: replace with a signed JWT token in Phase 2
                    return response;
                })
                .orElse(Collections.singletonMap("role", "INVALID"));
    }
}