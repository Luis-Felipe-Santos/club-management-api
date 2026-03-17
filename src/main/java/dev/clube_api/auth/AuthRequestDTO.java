package dev.clube_api.auth;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String senha;
}
