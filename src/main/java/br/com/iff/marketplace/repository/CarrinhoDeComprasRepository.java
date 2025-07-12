package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.CarrinhoDeCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoDeComprasRepository extends JpaRepository<CarrinhoDeCompras, Long> {

    // Método para encontrar um carrinho pelo ID do usuário
    Optional<CarrinhoDeCompras> findByUsuarioId(Long usuarioId);
}