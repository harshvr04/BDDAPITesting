package model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestModel {
    // Getters and setters
    private String username;
    private String password;

    public AuthRequestModel() {}

    public AuthRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

}