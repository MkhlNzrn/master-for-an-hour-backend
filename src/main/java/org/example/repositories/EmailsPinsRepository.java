package org.example.repositories;

import org.example.entities.EmailPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailsPinsRepository extends JpaRepository<EmailPin, Long> {
    boolean existsByEmail(String email);

    Optional<EmailPin> findByEmail(String email);
}
