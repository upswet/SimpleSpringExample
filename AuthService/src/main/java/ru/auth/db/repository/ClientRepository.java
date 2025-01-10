package ru.auth.db.repository;

import org.springframework.data.repository.CrudRepository;
import ru.auth.db.entity.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<ClientEntity, String> {
    Optional<ClientEntity> findByClientIdAndAudience(String clientId, String audience);
}
