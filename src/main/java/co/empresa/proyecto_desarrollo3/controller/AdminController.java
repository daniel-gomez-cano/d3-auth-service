package co.empresa.proyecto_desarrollo3.controller;

import co.empresa.proyecto_desarrollo3.dto.AdminDTO;
import co.empresa.proyecto_desarrollo3.dto.ClientDTO;
import co.empresa.proyecto_desarrollo3.dto.OrganizerDTO;
import co.empresa.proyecto_desarrollo3.service.AdminService;
import co.empresa.proyecto_desarrollo3.service.ClientService;
import co.empresa.proyecto_desarrollo3.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // Todos los endpoints de este controller requieren ADMIN
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrganizerService organizerService;

    /**
     * Endpoint de prueba — usado en los tests del pipeline.
     * Tests: adminConRolAdmin(), adminConRolCliente()
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Admin endpoint OK");
    }

    /**
     * Perfil del admin autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<AdminDTO> miPerfil(@AuthenticationPrincipal Jwt jwt) {
        AdminDTO dto = adminService.obtenerPorKeycloakId(jwt.getSubject());
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista todos los clientes — solo ADMIN.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> listarClientes() {
        return ResponseEntity.ok(clientService.listarTodos());
    }

    /**
     * Lista todos los organizadores — solo ADMIN.
     */
    @GetMapping("/organizers")
    public ResponseEntity<List<OrganizerDTO>> listarOrganizadores() {
        return ResponseEntity.ok(organizerService.listarTodos());
    }

    /**
     * Verifica un organizador manualmente — solo ADMIN.
     * Permite publicar eventos en la plataforma.
     */
    @PutMapping("/organizers/{keycloakId}/verify")
    public ResponseEntity<String> verificarOrganizador(@PathVariable String keycloakId) {
        organizerService.verificar(keycloakId);
        return ResponseEntity.ok("Organizador verificado: " + keycloakId);
    }

    /**
     * Desactiva un usuario — solo ADMIN.
     */
    @PutMapping("/users/{keycloakId}/deactivate")
    public ResponseEntity<String> desactivarUsuario(@PathVariable String keycloakId) {
        adminService.desactivarUsuario(keycloakId);
        return ResponseEntity.ok("Usuario desactivado: " + keycloakId);
    }
}
