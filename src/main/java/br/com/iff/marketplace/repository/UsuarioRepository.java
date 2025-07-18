package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);

    Usuario findUsuarioByEmail(String email);

    Usuario findBySenhaResetToken(String token);

    @Query(value = "SELECT * FROM usuario WHERE id = :id", nativeQuery = true)
    Optional<Usuario> findByIdEvenIfInactive(Long id);

    @Query(value = "SELECT * FROM usuario", nativeQuery = true)
    List<Usuario> findAllEvenIfInactive();
}