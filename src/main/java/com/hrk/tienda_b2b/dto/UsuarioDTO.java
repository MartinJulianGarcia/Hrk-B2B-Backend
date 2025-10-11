package com.hrk.tienda_b2b.dto;

import com.hrk.tienda_b2b.model.TipoUsuario;
import com.hrk.tienda_b2b.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDTO {
    private Long id;
    private String nombreRazonSocial;
    private String cuit;
    private String email;
    private TipoUsuario tipoUsuario;
    private LocalDateTime fechaCreacion;
    private Boolean activo;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombreRazonSocial(usuario.getNombreRazonSocial())
                .cuit(usuario.getCuit())
                .email(usuario.getEmail())
                .tipoUsuario(usuario.getTipoUsuario())
                .fechaCreacion(usuario.getFechaCreacion())
                .activo(usuario.getActivo())
                .build();
    }
}