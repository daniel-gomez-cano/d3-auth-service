package co.empresa.proyecto_desarrollo3.dto;


import co.empresa.proyecto_desarrollo3.model.Client;

import java.time.LocalDateTime;

public class ClientDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String documentId;
    private Boolean active;
    private LocalDateTime createdAt;

    public ClientDTO() {}

    public static ClientDTO fromEntity(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.id = client.getId();
        dto.email = client.getEmail();
        dto.firstName = client.getFirstName();
        dto.lastName = client.getLastName();
        dto.phone = client.getPhone();
        dto.documentId = client.getDocumentId();
        dto.active = client.getActive();
        dto.createdAt = client.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
