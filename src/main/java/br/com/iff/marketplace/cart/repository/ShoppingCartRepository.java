package br.com.iff.marketplace.cart.repository;

import br.com.iff.marketplace.cart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    // Método para encontrar um carrinho pelo ID do usuário
    Optional<ShoppingCart> findByUserId(Long userId);
}