package com.kwetter.userservice.entity;

public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
