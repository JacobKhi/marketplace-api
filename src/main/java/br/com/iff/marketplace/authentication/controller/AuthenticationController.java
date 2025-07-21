package br.com.iff.marketplace.authentication.controller;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.authentication.dto.LoginRequestDTO;
import br.com.iff.marketplace.authentication.dto.LoginResponseDTO;
import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.authentication.service.TokenService;
import br.com.iff.marketplace.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final UserService userService;
    private final TokenService tokenService;

    // Endpoint para entrar com um usuario existente
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );

        Authentication authentication = manager.authenticate(authenticationToken);

        String tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(tokenJWT));
    }

    // Endpoint para cadastrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid CreateUserDTO userDTO) {

        User registeredUser = userService.createUser(userDTO);
        UserResponseDTO createdUser = new UserResponseDTO(registeredUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}