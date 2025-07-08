package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);

    Usuario findUsuarioByEmail(String email);

    Usuario findBySenhaResetToken(String token);
}