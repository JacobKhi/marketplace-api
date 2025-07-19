package br.com.iff.marketplace.user.controller;


import br.com.iff.marketplace.controller.dto.UserResponseDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.service.UserService;
import br.com.iff.marketplace.user.dto.UpdateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/users/profile")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    // Endpoint para buscar os dados do usuário logado
    @GetMapping
    public ResponseEntity<UserResponseDTO> getOwnProfile(Authentication authentication) {
        User currentUser  = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new UserResponseDTO(currentUser));
    }

    // Endpoint para atualizar um usuário existente
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUserProfile(@RequestBody @Valid UpdateUserDTO userDTO, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        User updatedUser = userService.updateUserProfile(currentUser.getId(), userDTO);

        return ResponseEntity.ok(new UserResponseDTO(updatedUser));
    }

}
