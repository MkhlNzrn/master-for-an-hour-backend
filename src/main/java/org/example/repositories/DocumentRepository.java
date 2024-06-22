package org.example.repositories;

import org.example.entities.Document;
import org.example.entities.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByMaster(Master master);
}
