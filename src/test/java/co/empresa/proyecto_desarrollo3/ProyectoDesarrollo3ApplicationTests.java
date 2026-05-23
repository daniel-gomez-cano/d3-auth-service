package co.empresa.proyecto_desarrollo3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // sin esto, Spring intenta conectar a Keycloak real al arrancar
class ProyectoDesarrollo3ApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring arranca correctamente
        // con la configuración de test (H2 + Keycloak mockeado)
    }
}
