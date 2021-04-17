package com.example.myapp.network.account.dto;

public class RegistrationDTO {
    private String displayName;
    private String userName;
    private String email;
    private String password;

    public RegistrationDTO() {
    }

    public RegistrationDTO(String displayName, String userName, String email, String password) {
        this.displayName = displayName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
