package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.iff.marketplace.controller.dto.UsuarioResponseDTO;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService service;

    // Endpoint para CADASTRAR um novo usuário
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid CreateUserDTO user) {
        Usuario registeredUser  = service.createUser(user);
        // 201 para usuário criado com sucesso
        return ResponseEntity.status(201).body(new UsuarioResponseDTO(registeredUser));
    }

    // Endpoint para buscar os dados do usuário logado
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> buscarDadosUsuarioLogado() {
        Usuario usuarioLogado = service.buscarUsuarioLogado();

        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioLogado));
    }

    // Endpoint para LISTAR todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = service.findAll(false).stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para ATUALIZAR um usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = service.atualizarUsuario(id, usuario);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
    }






}