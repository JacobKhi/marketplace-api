package br.com.iff.marketplace.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {

    @NotBlank(message = "O email não pode estar em branco")
    @Email
    private String email;

}
