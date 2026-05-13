package co.empresa.proyecto_desarrollo3.auth.service;

import co.empresa.proyecto_desarrollo3.model.Admin;
import co.empresa.proyecto_desarrollo3.model.Client;
import co.empresa.proyecto_desarrollo3.model.Organizer;
import co.empresa.proyecto_desarrollo3.repository.AdminRepository;
import co.empresa.proyecto_desarrollo3.repository.ClientRepository;
import co.empresa.proyecto_desarrollo3.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class AuthService {

    @Autowired private RestTemplate restTemplate;
    @Autowired private ClientRepository clientRepository;
    @Autowired private OrganizerRepository organizerRepository;
    @Autowired private AdminRepository adminRepository;

    @Value("${keycloak.token-url}")
    private String tokenUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    // ── LOGIN ──────────────────────────────────────────────────────────────
    public String login(String username, String password) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "password");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("username", username);
            params.add("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(APPLICATION_FORM_URLENCODED);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    tokenUrl, new HttpEntity<>(params, headers), String.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error de autenticación: " + e.getMessage());
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Keycloak no disponible");
        }
    }

    // ── REFRESH ────────────────────────────────────────────────────────────
    public String refresh(String refreshToken) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("refresh_token", refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(APPLICATION_FORM_URLENCODED);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    tokenUrl, new HttpEntity<>(params, headers), String.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token de refresco inválido o expirado");
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Keycloak no disponible");
        }
    }

    // ── REGISTER ───────────────────────────────────────────────────────────
    @Transactional
    public void register(RegisterRequest req) {

        // 1. Token de admin
        String adminToken = obtenerTokenAdmin();

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(adminToken);

        // 2. Crear usuario en Keycloak
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", req.getUsername());
        userPayload.put("email", req.getEmail());
        userPayload.put("firstName", req.getFirstName());
        userPayload.put("lastName", req.getLastName());
        userPayload.put("enabled", true);
        userPayload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", req.getPassword(),
                "temporary", false
        )));

        String usersUrl = keycloakServerUrl + "/admin/realms/viva-eventos/users";
        ResponseEntity<Void> createResp = restTemplate.postForEntity(
                usersUrl, new HttpEntity<>(userPayload, authHeaders), Void.class);

        if (createResp.getStatusCode() != HttpStatus.CREATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo crear el usuario en Keycloak");
        }

        // 3. Extraer keycloakId del header Location
        String location = createResp.getHeaders().getLocation().toString();
        String keycloakId = location.substring(location.lastIndexOf('/') + 1);

        // 4. Asignar rol en Keycloak
        String roleUrl = keycloakServerUrl + "/admin/realms/viva-eventos/roles/" + req.getRole();
        ResponseEntity<Map> roleResp = restTemplate.exchange(
                roleUrl, HttpMethod.GET, new HttpEntity<>(authHeaders), Map.class);

        String assignUrl = keycloakServerUrl + "/admin/realms/viva-eventos/users/" + keycloakId + "/role-mappings/realm";
        restTemplate.postForEntity(
                assignUrl, new HttpEntity<>(List.of(roleResp.getBody()), authHeaders), Void.class);

        // 5. Guardar en BD local según el rol
        // Esto permite que otros microservicios consulten datos del usuario
        // por keycloakId sin tener que ir a Keycloak en cada request
        guardarEnBdLocal(keycloakId, req);
    }

    // ── HELPERS ────────────────────────────────────────────────────────────

    private void guardarEnBdLocal(String keycloakId, RegisterRequest req) {
        switch (req.getRole().toUpperCase()) {
            case "CLIENT" -> {
                Client client = new Client(
                        keycloakId,
                        req.getEmail(),
                        req.getFirstName(),
                        req.getLastName()
                );
                clientRepository.save(client);
            }
            case "EVENT_CREATOR" -> {
                // organizationName por defecto es el username hasta que lo actualice
                Organizer organizer = new Organizer(
                        keycloakId,
                        req.getEmail(),
                        req.getFirstName(),
                        req.getLastName(),
                        req.getUsername()
                );
                organizerRepository.save(organizer);
            }
            case "ADMIN" -> {
                Admin admin = new Admin(
                        keycloakId,
                        req.getEmail(),
                        req.getFirstName(),
                        req.getLastName()
                );
                adminRepository.save(admin);
            }
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rol inválido: " + req.getRole() + ". Valores permitidos: CLIENT, EVENT_CREATOR, ADMIN"
            );
        }
    }

    private String obtenerTokenAdmin() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "admin-cli");
        params.add("username", adminUsername);
        params.add("password", adminPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String adminTokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(
                adminTokenUrl, new HttpEntity<>(params, headers), Map.class);

        return (String) response.getBody().get("access_token");
    }
}
