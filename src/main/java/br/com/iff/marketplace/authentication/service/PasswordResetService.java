package br.com.iff.marketplace.authentication.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String generatePasswordResetToken(String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException("Usuário de email: " + email + " não encontrado");
        }

        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusMinutes(30));

        userRepository.save(user);
        return token;
    }

    public void resetPassword(
            String token,
            String newPswrd) {

        User user = userRepository.findByPasswordResetToken(token);

        if (user == null || user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String encryptedPswrd = passwordEncoder.encode(newPswrd);
        user.setPassword(encryptedPswrd);

        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiration(null);

        userRepository.save(user);
    }

}
