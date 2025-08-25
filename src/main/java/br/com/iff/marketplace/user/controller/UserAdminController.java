package br.com.iff.marketplace.user.controller;

import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.user.service.UserService;
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
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {
        List<UserResponseDTO> usuarios = userService.findAll(true).stream()
                .map(UserResponseDTO::new)
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

    @GetMapping("/seller-requests")
    public ResponseEntity<List<UserResponseDTO>> listPendingSellerRequests() {
        List<UserResponseDTO> pendingRequests = userService.findPendingSellerRequests().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pendingRequests);
    }

    // Endpoint para APROVAR uma solicitação de vendedor
    @PostMapping("/{userId}/approve-seller")
    public ResponseEntity<String> approveSellerRequest(@PathVariable Long userId) {
        userService.approveSellerRequest(userId);
        return ResponseEntity.ok("Solicitação de vendedor para o usuário " + userId + " foi aprovada com sucesso.");
    }

    // Endpoint para REJEITAR uma solicitação de vendedor
    @PostMapping("/{userId}/reject-seller")
    public ResponseEntity<String> rejectSellerRequest(@PathVariable Long userId) {
        userService.rejectSellerRequest(userId);
        return ResponseEntity.ok("Solicitação de vendedor para o usuário " + userId + " foi rejeitada.");
    }

}
