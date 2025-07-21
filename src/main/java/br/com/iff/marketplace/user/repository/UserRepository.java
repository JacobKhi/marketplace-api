package br.com.iff.marketplace.user.repository;

import br.com.iff.marketplace.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    User findUserByEmail(String email);

    User findByPasswordResetToken(String token);

    @Query(value = "SELECT * FROM usuario WHERE id = :id", nativeQuery = true)
    Optional<User> findByIdEvenIfInactive(Long id);

    @Query(value = "SELECT * FROM usuario", nativeQuery = true)
    List<User> findAllEvenIfInactive();
}