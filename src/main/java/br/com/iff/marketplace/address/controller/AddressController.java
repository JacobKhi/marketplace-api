package br.com.iff.marketplace.address.controller;

import br.com.iff.marketplace.address.dto.AddressRequestDTO;
import br.com.iff.marketplace.address.dto.AddressResponseDTO;
import br.com.iff.marketplace.address.service.AddressService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<Page<AddressResponseDTO>> listMyAddresses(
            @AuthenticationPrincipal User user,
            Pageable pageable) {

        Page<AddressResponseDTO> addressesPage = addressService.listAllAddresses(user, pageable);
        return ResponseEntity.ok(addressesPage);
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(
            @RequestBody @Valid AddressRequestDTO addressDTO,
            @AuthenticationPrincipal User user) {

        AddressResponseDTO newAddress = addressService.addAddress(addressDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @RequestBody @Valid AddressRequestDTO addressDTO,
            @AuthenticationPrincipal User user) {

        AddressResponseDTO updatedAddress = addressService.updateAddress(addressId, addressDTO, user);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal User user){

        addressService.deleteAddress(addressId, user);
        return ResponseEntity.ok().build();
    }

}
