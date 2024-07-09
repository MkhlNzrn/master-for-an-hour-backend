package org.example.repositories;

import org.example.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    boolean existsByMasterAndTask(Master master, Task task);

    void deleteAllByTask(Task task);

    List<Bid> findAllByTask(Task task);
}
