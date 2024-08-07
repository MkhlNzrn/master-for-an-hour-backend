package org.example.repositories;

import org.example.entities.Category;
import org.example.entities.Task;
import org.example.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select t from Task t")
    Page<Task> findAllTasksPage(Pageable pageable);

    @Query(value = "select t from Task t where t.category = ?1")
    List<Task> findAllByCategory(Category category);

    @Query(value = "select t from Task t where t.client = ?1")
    List<Task> findAllByClientsUserId(User client);

    @Query(value = "select t from Task t where t.master = ?1")
    List<Task> findAllByMastersUserId(User master);

    @Query(value = "select * from tasks where master_id = ?1", nativeQuery = true)
    List<Task> findAllByMastersUserId(Long masterUserId);
}
