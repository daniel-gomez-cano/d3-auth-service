package co.empresa.proyecto_desarrollo3.repository;

import co.empresa.proyecto_desarrollo3.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    Optional<Organizer> findByKeycloakId(String keycloakId);

    Optional<Organizer> findByEmail(String email);

    List<Organizer> findByVerifiedTrue();

    List<Organizer> findByActiveTrue();

    List<Organizer> findByVerifiedTrueAndActiveTrue();
}

