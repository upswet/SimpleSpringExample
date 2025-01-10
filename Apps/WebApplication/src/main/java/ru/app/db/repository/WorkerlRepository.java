package ru.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.app.db.entity.WorkerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerlRepository extends JpaRepository<WorkerEntity, Long> {
    Optional<WorkerEntity> findById(Long id);
    List<WorkerEntity> findAll();
}
