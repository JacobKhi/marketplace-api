package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios") // A URL base agora é /usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // Endpoint para CADASTRAR um novo usuário
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = service.salvarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Endpoint para LISTAR todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = service.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}