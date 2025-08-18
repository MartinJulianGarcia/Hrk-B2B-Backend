package com.hrk.tienda_b2b.controller;
import com.hrk.tienda_b2b.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.hrk.tienda_b2b.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    //public PedidoController(PedidoService pedidoService) {
    //   this.pedidoService = pedidoService;
    //}

    @PostMapping("/crear")
    public ResponseEntity<Pedido> crear(@RequestParam Long clienteId) {
        return ResponseEntity.ok(pedidoService.crearPedido(clienteId));
    }

    @PostMapping("/{pedidoId}/items")
    public ResponseEntity<Pedido> agregarItem(@PathVariable Long pedidoId,
                                              @RequestParam Long varianteId,
                                              @RequestParam int cantidad) {
        return ResponseEntity.ok(pedidoService.agregarItem(pedidoId, varianteId, cantidad));
    }

    @PostMapping("/{pedidoId}/confirmar")
    public ResponseEntity<Pedido> confirmar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.confirmar(pedidoId));
    }

    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.cancelar(pedidoId));
    }
}