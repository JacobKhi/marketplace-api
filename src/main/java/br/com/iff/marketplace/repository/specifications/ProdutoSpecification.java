package br.com.iff.marketplace.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.model.Produto;

import java.math.BigDecimal;

public class ProdutoSpecification {

    public static Specification<Produto> comNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }

        // Isso vai gerar uma cláusula 'WHERE' no SQL.
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Produto> comCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }

        // Pega a categoria, e da categoria, pega o ID.
        return (root, query, builder) ->
                builder.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Produto> comPrecoEntre(BigDecimal precoMin, BigDecimal precoMax) {
        if (precoMin == null && precoMax == null) {
            return null;
        }

        if (precoMin != null && precoMax == null) {
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("preco"), precoMin);
        }

        if (precoMin == null && precoMax != null) {
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("preco"), precoMax);
        }

        return (root, query, builder) -> builder.between(root.get("preco"), precoMin, precoMax);
    }

}