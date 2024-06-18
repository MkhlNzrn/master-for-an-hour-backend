package org.example.repositories;

import org.example.entities.Master;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
    @Query(value = "select m from Master m")
    Page<Master> findAllMastersPage(Pageable pageable);

    Master findByEmail(String email);
}
