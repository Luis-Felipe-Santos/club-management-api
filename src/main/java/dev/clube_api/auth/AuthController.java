package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, PasswordResetService passwordResetService){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody AuthRequestDTO dto,
            HttpServletResponse response
    ) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()
                )
        );

        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal();

        String accessToken = jwtService.gerarToken(usuario);
        RefreshToken refreshToken = refreshTokenService.create(usuario);

        // 🔐 cookie httpOnly
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        boolean isProd = false;

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        if (isProd) {
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
        }

        response.addCookie(cookie);

        return new AuthResponseDTO(accessToken);
    }
    @PostMapping("/refresh")
    public AuthResponseDTO refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new RuntimeException("Nenhum cookie encontrado");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Sessão expirada. Faça login novamente."));

        RefreshToken rt = refreshTokenService.validate(refreshToken);

        refreshTokenService.delete(rt);

        RefreshToken novo = refreshTokenService.create(rt.getUsuario());

        String newAccessToken = jwtService.gerarToken(rt.getUsuario());

        return new AuthResponseDTO(newAccessToken);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        passwordResetService.forgotPassword(request.email());

        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "message",
                        "Se o email estiver cadastrado, enviaremos as instruções para recuperação de senha."
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        passwordResetService.resetPassword(request);

        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "message",
                        "Senha redefinida com sucesso."
                )
        );
    }
}
