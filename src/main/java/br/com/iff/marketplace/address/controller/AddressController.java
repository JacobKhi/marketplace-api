package br.com.iff.marketplace.address.controller;

import br.com.iff.marketplace.address.dto.AddressRequestDTO;
import br.com.iff.marketplace.address.dto.AddressResponseDTO;
import br.com.iff.marketplace.address.service.AddressService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> listMyAddresses(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        List<AddressResponseDTO> foundAddresses = addressService.findAddressesByUser(user);

        return ResponseEntity.ok(foundAddresses);
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(
            @RequestBody @Valid AddressRequestDTO addressDTO,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        AddressResponseDTO newAddress = addressService.addAddress(addressDTO, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication){

        User user = (User) authentication.getPrincipal();
        addressService.deleteAddress(addressId, user);

        return ResponseEntity.ok().build();
    }

}
