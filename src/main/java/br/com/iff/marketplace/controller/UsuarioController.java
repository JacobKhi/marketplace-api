package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

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

    // Endpoint para o ADMIN deletar qualquer usuário
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para o ADMIN suspender ou reativar um usuário
    @PatchMapping("/{id}/ativacao")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> alterarStatusAtivo(@PathVariable Long id) {
        service.alterarStatusAtivo(id);
        return ResponseEntity.ok().build();
    }
}