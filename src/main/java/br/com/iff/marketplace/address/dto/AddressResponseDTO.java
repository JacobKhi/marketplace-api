package br.com.iff.marketplace.address.dto;

import br.com.iff.marketplace.address.Address;
import lombok.Data;

@Data
public class AddressResponseDTO {

    private Long id;

    private String zipCode;

    private String street;

    private String number;

    private String complement;

    private String city;

    private String state;

    public AddressResponseDTO(Address address) {
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.city = address.getCity();
        this.state = address.getState();
    }
}
