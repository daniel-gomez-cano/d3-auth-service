package co.empresa.proyecto_desarrollo3.service;

import co.empresa.proyecto_desarrollo3.dto.ClientDTO;
import co.empresa.proyecto_desarrollo3.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientDTO obtenerPorKeycloakId(String keycloakId) {
        return clientRepository.findByKeycloakId(keycloakId)
                .map(ClientDTO::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + keycloakId));
    }

    public List<ClientDTO> listarTodos() {
        return clientRepository.findByActiveTrue()
                .stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

