package br.com.iff.marketplace.authentication;

import br.com.iff.marketplace.authentication.dto.ForgotPasswordDTO;
import br.com.iff.marketplace.authentication.dto.ResetPasswordDTO;
import br.com.iff.marketplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserService userService;

    // Endpoint para quando esquecer a senha
    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid ForgotPasswordDTO dto) {
        String email = dto.getEmail();
        String token = userService.generatePasswordResetToken(email);

        String response = "Token de recuperação (para testes): " + token;

        return ResponseEntity.ok(response);
    }

    // Endpoint para resetar a senha
    @PostMapping("/reset-password")
    public ResponseEntity<String> performPasswordReset(@RequestBody @Valid ResetPasswordDTO dto) {
        String token = dto.getToken();
        String newPassword = dto.getNewPassword();

        userService.resetPassword(token, newPassword);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

}
