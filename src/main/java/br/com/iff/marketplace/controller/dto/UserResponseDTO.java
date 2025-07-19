package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.UserProfiles;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private boolean ativo;
    private UserProfiles perfil;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getName();
        this.email = user.getEmail();
        this.telefone = user.getPhoneNumber();
        this.ativo = user.isActive();
        this.perfil = user.getProfile();
    }
}