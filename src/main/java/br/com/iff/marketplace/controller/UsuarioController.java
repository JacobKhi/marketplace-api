package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

    // Endpoint para ATUALIZAR um usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario dadosParaAtualizar) {
        Usuario usuarioAtualizado = service.atualizarUsuario(id, dadosParaAtualizar);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> solicitarRecuperacaoSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String token = service.gerarTokenRecuperacaoSenha(email);

        String resposta = "Token de recuperação (para testes): " + token;
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> efetuarResetSenha(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String novaSenha = body.get("novaSenha");

        service.resetarSenha(token, novaSenha);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

    // Endpoint que SÓ o ADMIN pode acessar
    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Usuario>> listarTodosParaAdmin() {
        List<Usuario> usuarios = service.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}