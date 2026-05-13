package co.empresa.proyecto_desarrollo3;

import co.empresa.proyecto_desarrollo3.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("POST /auth/login — responde 200 con token simulado")
    void loginExitoso() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenReturn("{\"access_token\":\"fake-jwt\",\"token_type\":\"Bearer\"}");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"usuario1\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("access_token")));
    }

    @Test
    @DisplayName("POST /auth/login — sin credenciales responde 400")
    void loginSinCredenciales() throws Exception {
        // username y password están vacíos — AuthService lanzará excepción
        when(authService.login("", ""))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "Credenciales requeridas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /auth/register — responde 201")
    void registerExitoso() throws Exception {
        // register() devuelve void — por defecto Mockito no hace nada (correcto)
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "nuevo_usuario",
                              "email": "nuevo@test.com",
                              "password": "Password123!",
                              "firstName": "Juan",
                              "lastName": "Pérez",
                              "role": "CLIENT"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario creado exitosamente"));
    }

    @Test
    @DisplayName("POST /auth/refresh — responde 200 con nuevo token")
    void refreshExitoso() throws Exception {
        when(authService.refresh(anyString()))
                .thenReturn("{\"access_token\":\"nuevo-jwt\",\"token_type\":\"Bearer\"}");

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refresh_token\":\"fake-refresh-token\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("access_token")));
    }
}
