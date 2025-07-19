package br.com.iff.marketplace.service;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.model.enums.PerfilUsuario;
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

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User salvarUsuario(User user) {
        String senhaOriginal = user.getPassword();

        String senhaCriptografada = passwordEncoder.encode(senhaOriginal);

        user.setPassword(senhaCriptografada);

        return repository.save(user);
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
        newUser.setProfile(PerfilUsuario.CUSTOMER);

        return repository.save(newUser);
    }

    public String generatePasswordResetToken(String email) {
        User user = repository.findUsuarioByEmail(email);

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado com o e-mail fornecido.");
        }

        String token = UUID.randomUUID().toString();

        user.setSenhaResetToken(token);
        user.setSenhaResetTokenExpiracao(LocalDateTime.now().plusMinutes(30));

        repository.save(user);

        return token;
    }

    public void resetPassword(String token, String novaSenha) {
        User user = repository.findBySenhaResetToken(token);

        if (user == null || user.getSenhaResetTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        user.setPassword(senhaCriptografada);

        user.setSenhaResetToken(null);
        user.setSenhaResetTokenExpiracao(null);

        repository.save(user);
    }

    public User updateUserProfile(Long userId, UpdateUserDTO dto) {
        User userEncontrado = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));

        userEncontrado.setName(dto.getName());
        userEncontrado.setPhoneNumber(dto.getPhoneNumber());
        return repository.save(userEncontrado);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void toggleActivation(Long id) {
        User user = repository.findByIdEvenIfInactive(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        boolean estadoAtual = user.isActive();
        user.setActive(!estadoAtual);

        repository.save(user);
    }

    public List<User> findAll(boolean incluirInativos) {
        if (incluirInativos) {
            return repository.findAllEvenIfInactive();
        } else {
            return repository.findAll();
        }
    }


}