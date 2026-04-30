package co.empresa.proyecto_desarrollo3.model;

import co.empresa.proyecto_desarrollo3.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "organizers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Organizer extends User {

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "contact_phone")
    private String contactPhone;


    /* Este verificado puede ser removido*/
    @Column(nullable = false)
    private Boolean verified = false;

    public Organizer() {}

    public Organizer(String keycloakId, String email, String firstName,
                     String lastName, String organizationName) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_EVENT_CREATOR);
        this.organizationName = organizationName;
        this.verified = false;
    }

    public Organizer(String keycloakId, String email, String firstName, String lastName,
                     String organizationName, String contactPhone) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_EVENT_CREATOR);
        this.organizationName = organizationName;
        this.contactPhone = contactPhone;
        this.verified = false;
    }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
}