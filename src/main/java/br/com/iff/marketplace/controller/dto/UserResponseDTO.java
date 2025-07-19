package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.User;
import br.com.iff.marketplace.model.enums.PerfilUsuario;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private boolean ativo;
    private PerfilUsuario perfil;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.telefone = user.getTelefone();
        this.ativo = user.isAtivo();
        this.perfil = user.getPerfil();
    }
}