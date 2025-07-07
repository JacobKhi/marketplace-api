package br.com.iff.marketplace.service;

import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        String senhaOriginal = usuario.getSenha();

        String senhaCriptografada = passwordEncoder.encode(senhaOriginal);

        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }
}