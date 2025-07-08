package br.com.iff.marketplace.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.model.Produto;

public class ProdutoSpecification {

    public static Specification<Produto> comNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }

        // Isso vai gerar uma cláusula 'WHERE' no SQL.
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }
}