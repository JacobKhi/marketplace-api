package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    User findUsuarioByEmail(String email);

    User findBySenhaResetToken(String token);

    @Query(value = "SELECT * FROM usuario WHERE id = :id", nativeQuery = true)
    Optional<User> findByIdEvenIfInactive(Long id);

    @Query(value = "SELECT * FROM usuario", nativeQuery = true)
    List<User> findAllEvenIfInactive();
}