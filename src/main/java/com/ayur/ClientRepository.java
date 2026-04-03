//ClientRepository.java
package com.ayur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // ─────────────────────────────────────────────────────────────────
    // CRITICAL FIX: replaces the unsafe findAll() + stream filter
    // that was in LoginController.
    //
    // Spring Data JPA generates the SQL:
    //   SELECT * FROM clients WHERE username = ?
    // This is a single indexed lookup — far faster and safer than
    // loading every row into memory just to filter in Java.
    // ─────────────────────────────────────────────────────────────────
    Optional<Client> findByUsername(String username);

    // ── Role-based lookups ─────────────────────────────────────────────
    // Used by WebController and AdminController instead of
    // clientRepository.findAll() + manual stream filters.
    List<Client> findByRole(String role);

    // ── Existence checks ───────────────────────────────────────────────
    // Used by ClientController to check for duplicate usernames
    // without loading the full entity.
    boolean existsByUsername(String username);

    // ── Search (used by admin patient search) ──────────────────────────
    // JPQL query: case-insensitive name/branch search scoped to PATIENT role.
    // Replaces the stream().filter() chain in the old ClientController.getAll().
    @Query("""
            SELECT c FROM Client c
            WHERE c.role = 'PATIENT'
              AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(c.medicineBranch) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    List<Client> searchPatients(@Param("query") String query);

    // ── Count by role (used by AdminController /api/admin/stats) ──────
    long countByRole(String role);
}