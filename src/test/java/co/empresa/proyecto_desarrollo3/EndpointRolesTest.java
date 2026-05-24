package co.empresa.proyecto_desarrollo3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import co.empresa.proyecto_desarrollo3.service.AdminService;
import co.empresa.proyecto_desarrollo3.service.ClientService;
import co.empresa.proyecto_desarrollo3.service.OrganizerService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EndpointRolesTest {

    @Autowired
    private MockMvc mockMvc;

    // Mockeamos los servicios para que los controllers no intenten ir a la BD
    @MockBean private ClientService clientService;
    @MockBean private OrganizerService organizerService;
    @MockBean private AdminService adminService;

    // ── /client/** ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /client/test — CLIENT puede acceder")
    void clientConRolClient() throws Exception {
        mockMvc.perform(get("/client/test")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /client/test — sin token recibe 401")
    void clientSinToken() throws Exception {
        mockMvc.perform(get("/client/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /client/secure-method — ORGANIZER recibe 403")
    void clientEndpointConRolIncorrecto() throws Exception {
        mockMvc.perform(get("/client/secure-method")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /client/secure-method — CLIENT puede acceder")
    void clientSecureMethodConRolCorrecto() throws Exception {
        mockMvc.perform(get("/client/secure-method")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isOk());
    }

    // ── /creator/** ────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /creator/event/{id} — ORGANIZER con su propio ID puede acceder")
    void creatorConRolCorrecto() throws Exception {
        String organizerId = "uuid-123";
        mockMvc.perform(get("/creator/event/" + organizerId)
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))
                                .jwt(token -> token.subject(organizerId))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /creator/event/{id} — ORGANIZER con ID ajeno recibe 403")
    void creatorConIdAjeno() throws Exception {
        mockMvc.perform(get("/creator/event/otro-uuid")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))
                                .jwt(token -> token.subject("mi-uuid"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /creator/event/{id} — CLIENT recibe 403")
    void creatorConRolIncorrecto() throws Exception {
        mockMvc.perform(get("/creator/event/uuid-123")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /creator/event/{id} — sin token recibe 401")
    void creatorSinToken() throws Exception {
        mockMvc.perform(get("/creator/event/uuid-123"))
                .andExpect(status().isUnauthorized());
    }

    // ── /admin/** ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /admin/test — ADMIN puede acceder")
    void adminConRolAdmin() throws Exception {
        mockMvc.perform(get("/admin/test")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/test — CLIENT recibe 403")
    void adminConRolCliente() throws Exception {
        mockMvc.perform(get("/admin/test")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /admin/test — ORGANIZER recibe 403")
    void adminConRolCreator() throws Exception {
        mockMvc.perform(get("/admin/test")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /admin/test — sin token recibe 401")
    void adminSinToken() throws Exception {
        mockMvc.perform(get("/admin/test"))
                .andExpect(status().isUnauthorized());
    }
}
