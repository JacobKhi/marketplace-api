package br.com.iff.marketplace.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    @NotBlank(message = "O email não pode estar em branco")
    @Email
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String password;

    @NotBlank(message = "O número de telefone não pode estar em branco")
    private String phoneNumber;

    @NotBlank(message = "O documento não pode estar em branco")
    private String document; // Pode ser tanto CPF ou CNPJ

}
