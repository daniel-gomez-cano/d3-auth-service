package co.empresa.proyecto_desarrollo3.dto;

import co.empresa.proyecto_desarrollo3.model.Organizer;

import java.time.LocalDateTime;

public class OrganizerDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String contactPhone;
    private Boolean verified;
    private Boolean active;
    private LocalDateTime createdAt;

    public OrganizerDTO() {}

    public static OrganizerDTO fromEntity(Organizer organizer) {
        OrganizerDTO dto = new OrganizerDTO();
        dto.id = organizer.getId();
        dto.email = organizer.getEmail();
        dto.firstName = organizer.getFirstName();
        dto.lastName = organizer.getLastName();
        dto.organizationName = organizer.getOrganizationName();
        dto.contactPhone = organizer.getContactPhone();
        dto.verified = organizer.getVerified();
        dto.active = organizer.getActive();
        dto.createdAt = organizer.getCreatedAt();
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

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
