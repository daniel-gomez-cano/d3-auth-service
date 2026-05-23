package co.empresa.proyecto_desarrollo3.service;

import co.empresa.proyecto_desarrollo3.dto.OrganizerDTO;
import co.empresa.proyecto_desarrollo3.model.Organizer;
import co.empresa.proyecto_desarrollo3.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    public OrganizerDTO obtenerPorKeycloakId(String keycloakId) {
        return organizerRepository.findByKeycloakId(keycloakId)
                .map(OrganizerDTO::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Organizador no encontrado: " + keycloakId));
    }

    public List<OrganizerDTO> listarTodos() {
        return organizerRepository.findByActiveTrue()
                .stream()
                .map(OrganizerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un organizador puede publicar eventos.
     * Llamado por otros microservicios (event-service) antes de crear un evento.
     */
    public boolean estaVerificadoYActivo(String keycloakId) {
        return organizerRepository.findByKeycloakId(keycloakId)
                .map(o -> o.getVerified() && o.getActive())
                .orElse(false);
    }

    @Transactional
    public void verificar(String keycloakId) {
        Organizer organizer = organizerRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Organizador no encontrado: " + keycloakId));
        organizer.setVerified(true);
        organizerRepository.save(organizer);
    }
}

