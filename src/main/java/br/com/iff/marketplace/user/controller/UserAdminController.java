package br.com.iff.marketplace.user.controller;

import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping()
    public ResponseEntity<Page<UserResponseDTO>> listAllUsers(Pageable pageable) {

        Page<UserResponseDTO> usersPage = userAdminService.findAll(true, pageable).map(UserResponseDTO::new);
        return ResponseEntity.ok(usersPage);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        userAdminService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/activation")
    public ResponseEntity<Void> toggleUserActivation(@PathVariable Long userId) {

        userAdminService.toggleActivation(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seller-requests")
    public ResponseEntity<Page<UserResponseDTO>> listPendingSellerRequests(Pageable pageable) {

        Page<UserResponseDTO> pendingRequests = userAdminService.findPendingSellerRequests(pageable).map(UserResponseDTO::new);
        return ResponseEntity.ok(pendingRequests);
    }

    @PostMapping("/{userId}/approve-seller")
    public ResponseEntity<String> approveSellerRequest(@PathVariable Long userId) {

        userAdminService.approveSellerRequest(userId);
        return ResponseEntity.ok("Solicitação de vendedor para o usuário " + userId + " foi aprovada com sucesso.");
    }

    @PostMapping("/{userId}/reject-seller")
    public ResponseEntity<String> rejectSellerRequest(@PathVariable Long userId) {

        userAdminService.rejectSellerRequest(userId);
        return ResponseEntity.ok("Solicitação de vendedor para o usuário " + userId + " foi rejeitada.");
    }

}
