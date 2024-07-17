package org.example.repositories;

import org.example.entities.Master;
import org.example.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
    @Query(value = "select m from Master m")
    Page<Master> findAllMastersPage(Pageable pageable);

    Optional<Master> findByEmail(String email);

    Optional<Master> findByIdAndIsAcceptedFalse(Long id);

    boolean existsByEmail(String username);

    List<Master> findAllByIsAcceptedFalse();

    Optional<Master> findByUser(User user);


    @Query("SELECT m FROM Master m ORDER BY m.rate DESC")
    List<Master> findTop10ByOrderByRateDesc(Pageable pageable);
}
