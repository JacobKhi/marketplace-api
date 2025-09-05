package br.com.iff.marketplace.authentication.service;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.UserProfiles;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(CreateUserDTO userDTO) {

        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setDocument(userDTO.getDocument());

        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setProfile(UserProfiles.CUSTOMER);

        return userRepository.save(newUser);
    }

}
