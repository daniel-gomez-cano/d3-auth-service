package co.empresa.proyecto_desarrollo3.repository;

import co.empresa.proyecto_desarrollo3.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByKeycloakId(String keycloakId);

    Optional<Client> findByEmail(String email);

    List<Client> findByActiveTrue();

    boolean existsByDocumentId(String documentId);
}
