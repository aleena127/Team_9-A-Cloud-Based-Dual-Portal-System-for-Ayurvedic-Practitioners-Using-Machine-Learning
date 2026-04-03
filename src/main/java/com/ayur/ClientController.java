//ClientController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // ─────────────────────────────────────────────
    // Injected from your SecurityConfig @Bean.
    // BCryptPasswordEncoder hashes all passwords
    // before they ever touch the database.
    // ─────────────────────────────────────────────
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── List / Search ──────────────────────────────────────────────────────────
    // GET /api/clients           → all clients (admin use only)
    // GET /api/clients?search=x  → patients filtered by name or branch
    @GetMapping
    public List<Client> getAll(@RequestParam(required = false) String search) {
        List<Client> all = clientRepository.findAll();

        if (search != null && !search.isBlank()) {
            String query = search.toLowerCase();
            return all.stream()
                    .filter(c -> "PATIENT".equals(c.getRole()))
                    .filter(c -> c.getName() != null &&
                            (c.getName().toLowerCase().contains(query) ||
                             (c.getMedicineBranch() != null && c.getMedicineBranch().toLowerCase().contains(query))))
                    .collect(Collectors.toList());
        }

        return all;
    }

    // ── Create / Signup ────────────────────────────────────────────────────────
    // POST /api/clients
    // Security fixes applied here:
    //   1. Only PATIENT and DOCTOR roles are allowed from the public signup form.
    //      ADMIN accounts must be created directly in the DB or via a separate
    //      admin-only protected endpoint.
    //   2. Password is BCrypt-hashed before saving — never stored in plain text.
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Client client) {

        // ── SECURITY FIX 1: Block unauthorized role escalation ──
        // The old signup.html had an "__Admin__" option in the dropdown.
        // Even if you remove it from the UI, anyone can POST directly to this
        // endpoint with role=ADMIN. This server-side check prevents that.
        List<String> allowedPublicRoles = List.of("PATIENT", "DOCTOR");
        if (client.getRole() == null || !allowedPublicRoles.contains(client.getRole().toUpperCase())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Invalid role. Only PATIENT or DOCTOR accounts can be created here.");
        }

        // ── SECURITY FIX 2: Check for duplicate username ──
        if (clientRepository.findByUsername(client.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already taken. Please choose another.");
        }

        // ── SECURITY FIX 3: Hash the password with BCrypt ──
        // BCrypt automatically salts the hash — no need to manage salts yourself.
        // The stored hash looks like: $2a$10$...  (60 characters, never reversible)
        client.setPassword(passwordEncoder.encode(client.getPassword()));

        // Normalise role to uppercase for consistency
        client.setRole(client.getRole().toUpperCase());

        Client saved = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ── Update (doctor/admin use) ──────────────────────────────────────────────
    // PUT /api/clients/{id}
    // Only clinical fields are updatable — username, password, and role
    // are intentionally excluded from this endpoint to prevent tampering.
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id,
                                          @RequestBody Client details) {
        return clientRepository.findById(id)
                .map(client -> {
                    if (details.getName() != null)           client.setName(details.getName());
                    if (details.getMedicineBranch() != null) client.setMedicineBranch(details.getMedicineBranch());
                    if (details.getPrakriti() != null)       client.setPrakriti(details.getPrakriti());
                    if (details.getAgniStrength() != null)   client.setAgniStrength(details.getAgniStrength());
                    if (details.getProgressNotes() != null)  client.setProgressNotes(details.getProgressNotes());
                    if (details.getCurrentRoom() != null)    client.setCurrentRoom(details.getCurrentRoom());
                    if (details.getPrimaryDosha() != null)   client.setPrimaryDosha(details.getPrimaryDosha());
                    return ResponseEntity.ok(clientRepository.save(client));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Delete (admin only) ────────────────────────────────────────────────────
    // DELETE /api/clients/{id}
    // TODO: Add @PreAuthorize("hasRole('ADMIN')") once Spring Security is wired up.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Admin-only: create admin account ──────────────────────────────────────
    // POST /api/clients/admin/create
    // This endpoint should be protected by Spring Security (@PreAuthorize)
    // once you wire up the security config. It's the ONLY way to create an ADMIN.
    // TODO: Add @PreAuthorize("hasRole('ADMIN')") here.
    @PostMapping("/admin/create")
    public ResponseEntity<?> createAdmin(@RequestBody Client client) {
        if (clientRepository.findByUsername(client.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already taken.");
        }
        client.setRole("ADMIN");
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(clientRepository.save(client));
    }
    @PostMapping("/{id}/update-prakriti")
    @ResponseBody
    public String updatePrakriti(@PathVariable Long id,
                                 @RequestParam String primaryDosha,
                                 @RequestParam String agniStrength,
                                 @RequestParam String prakriti) {
        clientRepository.findById(id).ifPresent(c -> {
            c.setPrimaryDosha(primaryDosha);
            c.setAgniStrength(agniStrength);
            c.setPrakriti(prakriti);
            clientRepository.save(c);
        });
        return "OK";
    }
}