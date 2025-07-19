package br.com.iff.marketplace.service;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.model.enums.PerfilUsuario;
import br.com.iff.marketplace.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        String senhaOriginal = usuario.getPassword();

        String senhaCriptografada = passwordEncoder.encode(senhaOriginal);

        usuario.setPassword(senhaCriptografada);

        return repository.save(usuario);
    }

    public Usuario createUser(CreateUserDTO userDTO) {
        Usuario newUser = new Usuario();
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setDocument(userDTO.getDocument());
        // Obtém e criptografa a senha
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setProfile(PerfilUsuario.COMPRADOR);

        return repository.save(newUser);
    }

    public String generatePasswordResetToken(String email) {
        Usuario usuario = repository.findUsuarioByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado com o e-mail fornecido.");
        }

        String token = UUID.randomUUID().toString();

        usuario.setSenhaResetToken(token);
        usuario.setSenhaResetTokenExpiracao(LocalDateTime.now().plusMinutes(30));

        repository.save(usuario);

        return token;
    }

    public void resetPassword(String token, String novaSenha) {
        Usuario usuario = repository.findBySenhaResetToken(token);

        if (usuario == null || usuario.getSenhaResetTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuario.setPassword(senhaCriptografada);

        usuario.setSenhaResetToken(null);
        usuario.setSenhaResetTokenExpiracao(null);

        repository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario dadosParaAtualizar) {
        Usuario usuarioEncontrado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuarioEncontrado.setName(dadosParaAtualizar.getName());
        usuarioEncontrado.setPhoneNumber(dadosParaAtualizar.getPhoneNumber());
        return repository.save(usuarioEncontrado);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void toggleActivation(Long id) {
        Usuario usuario = repository.findByIdEvenIfInactive(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        boolean estadoAtual = usuario.isActive();
        usuario.setActive(!estadoAtual);

        repository.save(usuario);
    }

    public List<Usuario> findAll(boolean incluirInativos) {
        if (incluirInativos) {
            return repository.findAllEvenIfInactive();
        } else {
            return repository.findAll();
        }
    }

    public Usuario buscarUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}