package br.com.iff.marketplace.user.controller;


import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.service.UserProfileService;
import br.com.iff.marketplace.user.dto.UpdateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/profile")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getOwnProfile(@AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(new UserResponseDTO(currentUser));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUserProfile(
            @RequestBody @Valid UpdateUserDTO userDTO,
            @AuthenticationPrincipal User currentUser) {

        User updatedUser = userProfileService.updateUserProfile(currentUser.getId(), userDTO);
        return ResponseEntity.ok(new UserResponseDTO(updatedUser));
    }

    @PostMapping("/become-seller")
    public ResponseEntity<String> requestSellerProfile(@AuthenticationPrincipal User currentUser) {

        userProfileService.requestSellerProfile(currentUser.getId());
        return ResponseEntity.ok("Sua solicitação para se tornar um vendedor foi enviada e está aguardando análise.");
    }

}
