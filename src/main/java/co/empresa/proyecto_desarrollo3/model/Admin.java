package co.empresa.proyecto_desarrollo3.model;

import co.empresa.proyecto_desarrollo3.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Column(name = "department")
    private String department;

    @Column(name = "super_admin", nullable = false)
    private Boolean superAdmin = false;

    public Admin() {}

    public Admin(String keycloakId, String email, String firstName, String lastName) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_ADMIN);
        this.superAdmin = false;
    }

    public Admin(String keycloakId, String email, String firstName,
                 String lastName, String department, Boolean superAdmin) {
        super(keycloakId, email, firstName, lastName, RoleType.ROLE_ADMIN);
        this.department = department;
        this.superAdmin = superAdmin;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Boolean getSuperAdmin() { return superAdmin; }
    public void setSuperAdmin(Boolean superAdmin) { this.superAdmin = superAdmin; }
}