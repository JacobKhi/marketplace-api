package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.model.enums.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private PerfilUsuario perfil;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.perfil = usuario.getPerfil();
    }
}