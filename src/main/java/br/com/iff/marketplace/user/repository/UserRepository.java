package br.com.iff.marketplace.user.repository;

import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    User findUserByEmail(String email);

    User findByPasswordResetToken(String token);

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    Optional<User> findByIdEvenIfInactive(Long id);

    @Query(value = "SELECT * FROM users", nativeQuery = true)
    Page<User> findAllEvenIfInactive(Pageable pageable);

    Page<User> findBySellerStatus(SellerStatus status, Pageable pageable);

}