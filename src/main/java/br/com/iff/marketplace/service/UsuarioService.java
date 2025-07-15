package br.com.iff.marketplace.service;

import br.com.iff.marketplace.model.Usuario;
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
public class UsuarioService {

    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        String senhaOriginal = usuario.getSenha();

        String senhaCriptografada = passwordEncoder.encode(senhaOriginal);

        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    public String gerarTokenRecuperacaoSenha(String email) {
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

    public void resetarSenha(String token, String novaSenha) {
        Usuario usuario = repository.findBySenhaResetToken(token);

        if (usuario == null || usuario.getSenhaResetTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);

        usuario.setSenhaResetToken(null);
        usuario.setSenhaResetTokenExpiracao(null);

        repository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario dadosParaAtualizar) {
        Usuario usuarioEncontrado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuarioEncontrado.setNome(dadosParaAtualizar.getNome());
        usuarioEncontrado.setTelefone(dadosParaAtualizar.getTelefone());
        return repository.save(usuarioEncontrado);
    }

    public void deletarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void alterarStatusAtivo(Long id) {
        Usuario usuario = repository.findByIdEvenIfInactive(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        boolean estadoAtual = usuario.isAtivo();
        usuario.setAtivo(!estadoAtual);

        repository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario buscarUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}