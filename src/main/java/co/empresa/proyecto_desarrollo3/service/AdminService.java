package co.empresa.proyecto_desarrollo3.service;

import co.empresa.proyecto_desarrollo3.dto.AdminDTO;
import co.empresa.proyecto_desarrollo3.model.User;
import co.empresa.proyecto_desarrollo3.repository.AdminRepository;
import co.empresa.proyecto_desarrollo3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    public AdminDTO obtenerPorKeycloakId(String keycloakId) {
        return adminRepository.findByKeycloakId(keycloakId)
                .map(AdminDTO::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin no encontrado: " + keycloakId));
    }

    @Transactional
    public void desactivarUsuario(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado: " + keycloakId));
        user.setActive(false);
        userRepository.save(user);
    }
}
