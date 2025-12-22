package dev.clube_api.usuario.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO dto) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()
                )
        );

        UsuarioModel usuarioModel = (UsuarioModel) authentication.getPrincipal();

        String token = jwtService.gerarToken(usuarioModel);

        return new AuthResponseDTO(token);
    }
}
