package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.authentication.dto.CreateUserDTO;
import br.com.iff.marketplace.controller.dto.LoginDTO;
import br.com.iff.marketplace.controller.dto.UserResponseDTO;
import br.com.iff.marketplace.model.User;
import br.com.iff.marketplace.service.TokenService;
import br.com.iff.marketplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        // Se a autenticação for bem sucedida, gera o token
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(tokenJWT);
    }

    // Endpoint para CADASTRAR um novo usuário
    @PostMapping
    public ResponseEntity<UserResponseDTO> cadastrarUsuario(@RequestBody @Valid CreateUserDTO user) {
        User registeredUser  = userService.createUser(user);
        // 201 para usuário criado com sucesso
        return ResponseEntity.status(201).body(new UserResponseDTO(registeredUser));
    }

}