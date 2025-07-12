package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.CarrinhoDeComprasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoDeComprasItemRepository extends JpaRepository<CarrinhoDeComprasItem, Long> {
}