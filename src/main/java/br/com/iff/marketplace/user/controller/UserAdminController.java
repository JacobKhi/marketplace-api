package br.com.iff.marketplace.user.controller;

import br.com.iff.marketplace.controller.dto.UsuarioResponseDTO;
import br.com.iff.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    // Endpoint para ver todos os usuários
    @GetMapping()
    public ResponseEntity<List<UsuarioResponseDTO>> listAllUsers() {
        List<UsuarioResponseDTO> usuarios = userService.findAll(true).stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para deletar qualquer usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para suspender ou reativar um usuário
    @PatchMapping("/{id}/activation")
    public ResponseEntity<Void> toggleUserActivation(@PathVariable Long id) {
        userService.toggleActivation(id);
        return ResponseEntity.ok().build();
    }
}
