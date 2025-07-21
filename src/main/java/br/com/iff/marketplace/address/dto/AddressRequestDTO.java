package br.com.iff.marketplace.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequestDTO {

    @NotBlank(message = "O campo código postal não pode estar vazio")
    private String zipCode;

    @NotBlank(message = "O campo rua não pode estar vazio")
    private String street;

    @NotBlank(message = "O campo número não pode estar vazio")
    private String number;

    private String complement;

    @NotBlank(message = "O campo cidade não pode estar vazio")
    private String city;

    @NotBlank(message = "O campo estado não pode estar vazio")
    private String state;

}
