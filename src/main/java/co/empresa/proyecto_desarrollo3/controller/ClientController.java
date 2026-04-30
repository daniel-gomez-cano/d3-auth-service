package co.empresa.proyecto_desarrollo3.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/test")
    public String test() {
        return "Client endpoint";
    }

    //  Solo usuarios con rol USER
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/secure-method")
    public String metodoSeguro() {
        return "Metodo seguro CLIENT";
    }

    //  USER + validación de propiedad (organizerId)
    @PreAuthorize("hasRole('CLIENT') and #id == authentication.token.claims['sub']")
    @GetMapping("/secure/{id}")
    public String recursoPropio(@PathVariable String id, Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        return "Accediste a tu propio recurso. ID usuario: " + userId;
    }
}