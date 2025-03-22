package org.example.repositories;

import org.example.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    boolean existsByMasterAndTask(Master master, Task task);

    void deleteAllByTask(Task task);

    List<Bid> findAllByTask(Task task);

    boolean existsByTaskId(Long taskId);

    @Modifying
    @Transactional
    void deleteAllByTaskId(Long id);
}
