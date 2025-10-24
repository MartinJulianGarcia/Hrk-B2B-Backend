package com.hrk.tienda_b2b.controller;

import com.hrk.tienda_b2b.dto.CreatePedidoRequest;
import com.hrk.tienda_b2b.dto.PedidoResponseDTO;
import com.hrk.tienda_b2b.service.PedidoService;
import com.hrk.tienda_b2b.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody CreatePedidoRequest request) {
        try {
            System.out.println("🔵 [PEDIDO CONTROLLER] Request recibido");
            System.out.println("🔵 [PEDIDO CONTROLLER] Cliente ID: " + request.getClienteId());
            System.out.println("🔵 [PEDIDO CONTROLLER] Usuario: " + request.getUsuario());

            // Validar que el cliente existe
            if (request.getClienteId() == null) {
                return ResponseEntity.badRequest().body(crearRespuestaError("Cliente ID es obligatorio"));
            }

            // ⭐ USAR EL SERVICIO REAL EN LUGAR DE MOCK
            Pedido pedidoCreado = pedidoService.crearPedido(request.getClienteId());

            // Convertir a DTO
            PedidoResponseDTO responseDTO = convertirPedidoADTO(pedidoCreado, request.getUsuario());

            System.out.println("✅ [PEDIDO CONTROLLER] Pedido creado exitosamente con ID: " + pedidoCreado.getId());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            System.out.println("🔴 [PEDIDO CONTROLLER] Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(crearRespuestaError("Error interno: " + e.getMessage()));
        }
    }

    @PostMapping("/{pedidoId}/items")
    public ResponseEntity<?> agregarItem(
            @PathVariable Long pedidoId,
            @RequestParam Long varianteId,
            @RequestParam Integer cantidad,
            @RequestBody(required = false) Object body) {

        System.out.println("🔵 [PEDIDO CONTROLLER] Agregando item al pedido " + pedidoId +
                " - variante: " + varianteId + ", cantidad: " + cantidad);

        try {
            // ⭐ USAR EL SERVICIO REAL EN LUGAR DE MOCK
            Pedido pedidoActualizado = pedidoService.agregarItem(pedidoId, varianteId, cantidad);

            // Convertir a DTO
            PedidoResponseDTO responseDTO = convertirPedidoADTO(pedidoActualizado, null);

            System.out.println("✅ [PEDIDO CONTROLLER] Item agregado exitosamente");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            System.out.println("🔴 [PEDIDO CONTROLLER] Error al agregar item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(crearRespuestaError("Error al agregar item: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerPedidosPorCliente(@RequestParam Long clienteId) {
        System.out.println("🔵 [PEDIDO CONTROLLER] Obteniendo pedidos para cliente: " + clienteId);

        try {
            // ⭐ USAR EL SERVICIO REAL EN LUGAR DE MOCK
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorCliente(clienteId);

            // Convertir a DTOs
            List<PedidoResponseDTO> pedidosDTO = pedidos.stream()
                    .map(pedido -> convertirPedidoADTO(pedido, null))
                    .collect(Collectors.toList());

            System.out.println("✅ [PEDIDO CONTROLLER] Encontrados " + pedidos.size() + " pedidos");

            return ResponseEntity.ok(pedidosDTO);

        } catch (Exception e) {
            System.out.println("🔴 [PEDIDO CONTROLLER] Error al obtener pedidos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(crearRespuestaError("Error al obtener pedidos: " + e.getMessage()));
        }
    }

    // Método helper para convertir Pedido a PedidoResponseDTO
    private PedidoResponseDTO convertirPedidoADTO(Pedido pedido, CreatePedidoRequest.UsuarioInfoDTO usuarioInfo) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .fecha(pedido.getFecha().toString())
                .estado(pedido.getEstado().toString())
                .total(pedido.getTotal())
                .usuario(usuarioInfo != null ?
                        com.hrk.tienda_b2b.dto.UsuarioDTO.builder()
                                .id(pedido.getUsuario().getId())
                                .nombreRazonSocial(usuarioInfo.getNombreRazonSocial())
                                .email(usuarioInfo.getEmail())
                                .build() : null)
                .build();
    }

    private Map<String, String> crearRespuestaError(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return error;
    }
}