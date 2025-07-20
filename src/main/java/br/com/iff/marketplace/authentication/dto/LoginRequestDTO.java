package br.com.iff.marketplace.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "O email não pode esta vazio")
    @Email
    private String email;

    @NotBlank(message = "A senha não pode estar vazia")
    private String password;

}