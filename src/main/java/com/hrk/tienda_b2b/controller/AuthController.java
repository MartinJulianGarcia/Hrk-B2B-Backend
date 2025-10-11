package com.hrk.tienda_b2b.controller;
import com.hrk.tienda_b2b.dto.AuthResponse;
import com.hrk.tienda_b2b.dto.LoginRequest;
import com.hrk.tienda_b2b.dto.RegisterRequest;
import com.hrk.tienda_b2b.dto.UsuarioDTO;
import com.hrk.tienda_b2b.model.Pedido;
import com.hrk.tienda_b2b.model.Usuario;
import com.hrk.tienda_b2b.security.JwtService;
import com.hrk.tienda_b2b.service.CarritoService;
import com.hrk.tienda_b2b.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = usuarioService.registrar(request);
            String token = jwtService.generateToken(usuario);

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .usuario(UsuarioDTO.fromEntity(usuario))
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = usuarioService.login(request);
            String token = jwtService.generateToken(usuario);

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .usuario(UsuarioDTO.fromEntity(usuario))
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtService.extractUsername(token.substring(7));
            Usuario usuario = usuarioService.obtenerPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}