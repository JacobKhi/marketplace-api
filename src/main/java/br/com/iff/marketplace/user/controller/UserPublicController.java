package br.com.iff.marketplace.user.controller;

import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserAdminService userAdminService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listAllPublicUsers(Pageable pageable) {

        Page<UserResponseDTO> usersPage = userAdminService.findAll(false, pageable).map(UserResponseDTO::new);
        return ResponseEntity.ok(usersPage);
    }

}
