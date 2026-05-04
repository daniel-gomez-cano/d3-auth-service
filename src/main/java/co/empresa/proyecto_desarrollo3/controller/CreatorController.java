package co.empresa.proyecto_desarrollo3.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creator")
public class CreatorController {

    @GetMapping("/event/{organizerId}")
    @PreAuthorize("#organizerId == #jwt.subject")
    public String getEvent(
            @PathVariable String organizerId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return "Acceso permitido al evento del organizer: " + organizerId;
    }
}