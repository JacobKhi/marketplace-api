package br.com.iff.marketplace.user.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.dto.UpdateUserDTO;
import br.com.iff.marketplace.user.enums.SellerStatus;
import br.com.iff.marketplace.user.enums.UserProfiles;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public User updateUserProfile(Long userId, UpdateUserDTO dto) {

        User userEncontrado = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário com o ID: " + userId + " não encontrado"));

        userEncontrado.setName(dto.getName());
        userEncontrado.setPhoneNumber(dto.getPhoneNumber());

        return userRepository.save(userEncontrado);
    }

    public void requestSellerProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + userId));

        if (user.getProfile() == UserProfiles.SELLER || user.getSellerStatus() == SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário já é um vendedor ou já possui uma solicitação pendente.");
        }

        user.setSellerStatus(SellerStatus.PENDING_APPROVAL);
        userRepository.save(user);
    }

}
