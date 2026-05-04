package co.empresa.proyecto_desarrollo3.dto;


import co.empresa.proyecto_desarrollo3.model.Admin;

import java.time.LocalDateTime;

public class AdminDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private Boolean active;
    private LocalDateTime createdAt;

    public AdminDTO() {}

    public static AdminDTO fromEntity(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.id = admin.getId();
        dto.email = admin.getEmail();
        dto.firstName = admin.getFirstName();
        dto.lastName = admin.getLastName();
        dto.department = admin.getDepartment();
        dto.active = admin.getActive();
        dto.createdAt = admin.getCreatedAt();
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

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}