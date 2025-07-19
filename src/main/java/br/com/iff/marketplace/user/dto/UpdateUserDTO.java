package br.com.iff.marketplace.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    @NotBlank(message = "O número de telefone não pode estar em branco")
    private String phoneNumber;

}
