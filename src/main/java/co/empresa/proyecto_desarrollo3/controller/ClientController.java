package co.empresa.proyecto_desarrollo3.controller;

import co.empresa.proyecto_desarrollo3.dto.ClientDTO;
import co.empresa.proyecto_desarrollo3.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Endpoint de prueba — cualquier CLIENT autenticado.
     * Tests: clientConRolClient(), clientSinToken()
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Client endpoint OK");
    }

    /**
     * Método seguro — solo CLIENT.
     * Tests: clientEndpointConRolIncorrecto()
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/secure-method")
    public ResponseEntity<String> metodoSeguro() {
        return ResponseEntity.ok("Método seguro CLIENT");
    }

    /**
     * Recurso propio — CLIENT dueño del recurso (keycloakId == sub del JWT).
     * Usado por otros microservicios para verificar identidad del cliente.
     */
    @PreAuthorize("hasRole('CLIENT') and #id == authentication.token.claims['sub']")
    @GetMapping("/secure/{id}")
    public ResponseEntity<String> recursoPropio(@PathVariable String id,
                                                @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Recurso del cliente: " + jwt.getSubject());
    }

    /**
     * Perfil del cliente autenticado — devuelve datos de la BD local.
     * Otros microservicios pueden llamar esto para enriquecer datos.
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<ClientDTO> miPerfil(@AuthenticationPrincipal Jwt jwt) {
        ClientDTO dto = clientService.obtenerPorKeycloakId(jwt.getSubject());
        return ResponseEntity.ok(dto);
    }
}
