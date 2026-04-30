package co.empresa.proyecto_desarrollo3.model;

import co.empresa.proyecto_desarrollo3.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "phone")
    private String phone;

    @Column(name = "document_id")
    private String documentId;

    public Client() {}

    public Client(String keycloakId, String email, String firstName, String lastName) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_CLIENT);
    }

    public Client(String keycloakId, String email, String firstName,
                  String lastName, String phone, String documentId) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_CLIENT);
        this.phone = phone;
        this.documentId = documentId;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}
