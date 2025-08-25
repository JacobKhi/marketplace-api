package br.com.iff.marketplace.user.service;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.SellerStatus;
import br.com.iff.marketplace.user.enums.UserProfiles;
import br.com.iff.marketplace.user.repository.UserRepository;
import br.com.iff.marketplace.user.dto.UpdateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        String senhaOriginal = user.getPassword();

        String senhaCriptografada = passwordEncoder.encode(senhaOriginal);

        user.setPassword(senhaCriptografada);

        return userRepository.save(user);
    }

    public User createUser(CreateUserDTO userDTO) {
        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setDocument(userDTO.getDocument());
        // Obtém e criptografa a senha
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setProfile(UserProfiles.CUSTOMER);

        return userRepository.save(newUser);
    }

    public String generatePasswordResetToken(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado com o e-mail fornecido.");
        }

        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusMinutes(30));

        userRepository.save(user);

        return token;
    }

    public void resetPassword(String token, String novaSenha) {
        User user = userRepository.findByPasswordResetToken(token);

        if (user == null || user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        user.setPassword(senhaCriptografada);

        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiration(null);

        userRepository.save(user);
    }

    public User updateUserProfile(Long userId, UpdateUserDTO dto) {
        User userEncontrado = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));

        userEncontrado.setName(dto.getName());
        userEncontrado.setPhoneNumber(dto.getPhoneNumber());
        return userRepository.save(userEncontrado);
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void toggleActivation(Long id) {
        User user = userRepository.findByIdEvenIfInactive(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        boolean estadoAtual = user.isActive();
        user.setActive(!estadoAtual);

        userRepository.save(user);
    }

    public List<User> findAll(boolean incluirInativos) {
        if (incluirInativos) {
            return userRepository.findAllEvenIfInactive();
        } else {
            return userRepository.findAll();
        }
    }

    public void requestSellerProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (user.getProfile() == UserProfiles.SELLER || user.getSellerStatus() == SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário já é um vendedor ou já possui uma solicitação pendente.");
        }

        user.setSellerStatus(SellerStatus.PENDING_APPROVAL);
        userRepository.save(user);
    }

    @Transactional
    public void approveSellerRequest(Long userId) {
        User user = userRepository.findByIdEvenIfInactive(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));

        if (user.getSellerStatus() != SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário não possui uma solicitação pendente para se tornar vendedor.");
        }

        user.setProfile(UserProfiles.SELLER);
        user.setSellerStatus(SellerStatus.APPROVED);
        userRepository.save(user);
    }

    @Transactional
    public void rejectSellerRequest(Long userId) {
        User user = userRepository.findByIdEvenIfInactive(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));

        if (user.getSellerStatus() != SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário não possui uma solicitação pendente.");
        }

        user.setSellerStatus(SellerStatus.REJECTED);
        userRepository.save(user);
    }

    public List<User> findPendingSellerRequests() {
        return userRepository.findBySellerStatus(SellerStatus.PENDING_APPROVAL);
    }
}