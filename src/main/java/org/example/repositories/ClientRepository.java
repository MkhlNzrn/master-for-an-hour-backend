package org.example.repositories;

import org.example.entities.Client;
import org.example.entities.Master;
import org.example.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String username);

    @Query(value = "select * from clients where user_id = ?1", nativeQuery = true)
    Optional<Client> findByUserId(Long id);

    @Query("select c from Client c")
    Page<Client> findAllClientsPage(Pageable pageable);
}
