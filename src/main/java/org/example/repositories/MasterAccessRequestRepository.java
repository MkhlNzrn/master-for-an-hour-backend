package org.example.repositories;

import org.example.entities.MasterAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterAccessRequestRepository extends JpaRepository<MasterAccessRequest, Long> {
}
