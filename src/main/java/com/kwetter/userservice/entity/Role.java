package com.kwetter.userservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {

    @Id
    @Column(unique = true)
    private String role;
    private String description;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Role(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
