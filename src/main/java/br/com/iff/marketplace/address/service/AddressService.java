package br.com.iff.marketplace.address.service;

import br.com.iff.marketplace.address.Address;
import br.com.iff.marketplace.address.dto.AddressRequestDTO;
import br.com.iff.marketplace.address.dto.AddressResponseDTO;
import br.com.iff.marketplace.address.repository.AddressRepository;
import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponseDTO> findAddressesByUser(User user) {

        List<Address> addresses = addressRepository.findByUserId(user.getId());

        return addresses.stream()
                .map(AddressResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AddressResponseDTO addAddress(AddressRequestDTO addressRequestDTO, User user) {

        Address newAddress = new Address();
        newAddress.setZipCode(addressRequestDTO.getZipCode());
        newAddress.setStreet(addressRequestDTO.getStreet());
        newAddress.setNumber(addressRequestDTO.getNumber());
        newAddress.setComplement(addressRequestDTO.getComplement());
        newAddress.setCity(addressRequestDTO.getCity());
        newAddress.setState(addressRequestDTO.getState());

        newAddress.setUser(user);

        Address savedAddress = addressRepository.save(newAddress);

        return new AddressResponseDTO(savedAddress);
    }

    public void deleteAddress(Long addressId, User user) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Endereço com id " + addressId + " não encontrado."));

        boolean isFromUser = address.getUser().getId().equals(user.getId());

        if (!isFromUser) {
            throw new AccessDeniedException("Você não tem permissão para deletar este endereço.");
        }

        addressRepository.delete(address);
    }

}
