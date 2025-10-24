package co.com.egonzalias.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String email;
    private String role;
}

