package co.empresa.proyecto_desarrollo3.repository;

import co.empresa.proyecto_desarrollo3.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByKeycloakId(String keycloakId);

    Optional<Admin> findByEmail(String email);

    List<Admin> findBySuperAdminTrue();

    List<Admin> findByActiveTrue();
}

