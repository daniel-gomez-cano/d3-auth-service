package co.empresa.proyecto_desarrollo3.controller;

import co.empresa.proyecto_desarrollo3.dto.OrganizerDTO;
import co.empresa.proyecto_desarrollo3.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creator")
public class CreatorController {

    @Autowired
    private OrganizerService organizerService;

    /**
     * Obtiene el evento de un organizer — solo el dueño puede verlo.
     * La doble validación (rol + sub) es el patrón correcto:
     *   1. hasRole asegura que es EVENT_CREATOR
     *   2. #organizerId == #jwt.subject asegura que es SU propio recurso
     *
     * Tests: creatorConRolCorrecto(), creatorConRolIncorrecto()
     */
    @PreAuthorize("hasRole('EVENT_CREATOR') and #organizerId == #jwt.subject")
    @GetMapping("/event/{organizerId}")
    public ResponseEntity<String> getEvent(@PathVariable String organizerId,
                                           @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Acceso permitido al organizer: " + organizerId);
    }

    /**
     * Perfil del organizador autenticado.
     * Otros microservicios (ej. event-service) llamarán esto para
     * validar que el creador existe y está verificado en la BD local.
     */
    @PreAuthorize("hasRole('EVENT_CREATOR')")
    @GetMapping("/me")
    public ResponseEntity<OrganizerDTO> miPerfil(@AuthenticationPrincipal Jwt jwt) {
        OrganizerDTO dto = organizerService.obtenerPorKeycloakId(jwt.getSubject());
        return ResponseEntity.ok(dto);
    }

    /**
     * Verifica si un organizer está activo y verificado.
     * Endpoint interno — otros microservicios lo consumen con su propio JWT.
     * Ejemplo: event-service verifica antes de permitir publicar un evento.
     */
    @PreAuthorize("hasRole('EVENT_CREATOR') or hasRole('ADMIN')")
    @GetMapping("/verify/{organizerId}")
    public ResponseEntity<Boolean> verificarOrganizer(@PathVariable String organizerId) {
        boolean activo = organizerService.estaVerificadoYActivo(organizerId);
        return ResponseEntity.ok(activo);
    }
}
