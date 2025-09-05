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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Page<AddressResponseDTO> listAllAddresses(
            User user,
            Pageable pageable) {

        Page<Address> addressesPage = addressRepository.findByUserId(user.getId(), pageable);
        return addressesPage.map(AddressResponseDTO::new);
    }

    public AddressResponseDTO addAddress(
            AddressRequestDTO addressRequestDTO,
            User user) {

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

    @Transactional
    public AddressResponseDTO updateAddress(
            Long addressId,
            AddressRequestDTO addressDTO,
            User user) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Endereço com id " + addressId + " não encontrado."));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar este endereço.");
        }

        address.setZipCode(addressDTO.getZipCode());
        address.setStreet(addressDTO.getStreet());
        address.setNumber(addressDTO.getNumber());
        address.setComplement(addressDTO.getComplement());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());

        Address savedAddress = addressRepository.save(address);
        return new AddressResponseDTO(savedAddress);
    }

    public void deleteAddress(
            Long addressId,
            User user) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Endereço com id " + addressId + " não encontrado."));

        boolean isFromUser = address.getUser().getId().equals(user.getId());

        if (!isFromUser) {
            throw new AccessDeniedException("Você não tem permissão para deletar este endereço.");
        }

        addressRepository.delete(address);
    }

}
