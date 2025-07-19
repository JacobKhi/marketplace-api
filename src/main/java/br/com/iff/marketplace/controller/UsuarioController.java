package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import br.com.iff.marketplace.controller.dto.UserResponseDTO;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> usuarios = userService.findAll(false).stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

}