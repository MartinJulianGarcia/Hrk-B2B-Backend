
package com.hrk.tienda_b2b.service;

import com.hrk.tienda_b2b.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hrk.tienda_b2b.repository.PedidoRepository;
import com.hrk.tienda_b2b.repository.ProductoVarianteRepository;
import com.hrk.tienda_b2b.repository.DetallePedidoRepository;
import com.hrk.tienda_b2b.repository.MovimientoStockRepository;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoVarianteRepository varianteRepo;
    private final DetallePedidoRepository detalleRepo;
    private final MovimientoStockRepository movRepo;

    //public PedidoServiceImpl(PedidoRepository pedidoRepo, ProductoVarianteRepository varianteRepo, DetallePedidoRepository detalleRepo, MovimientoStockRepository movRepo) {
    //   this.pedidoRepo = pedidoRepo;
    //    this.varianteRepo = varianteRepo;
    //    this.detalleRepo = detalleRepo;
  //      this.movRepo = movRepo;
  //  }

    @Override
    @Transactional
    public Pedido crearPedido(Long clienteId) {
        Pedido p = Pedido.builder()
                .clienteId(clienteId)
                .fecha(LocalDateTime.now())
                .estado(EstadoPedido.BORRADOR)
                .total(0.0)
                .build();
        return pedidoRepo.save(p);
    }

    @Override
    @Transactional
    public Pedido agregarItem(Long pedidoId, Long varianteId, int cantidad) {
        Pedido p = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (p.getEstado() != EstadoPedido.BORRADOR && p.getEstado() != EstadoPedido.DOCUMENTADO) {
            throw new IllegalStateException("No se pueden agregar items en estado " + p.getEstado());
        }

        ProductoVariante v = varianteRepo.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        // precio capturado al momento (ahora siempre está en la variante)
        Double precioUnitario = v.getPrecio();

        DetallePedido d = DetallePedido.builder()
                .pedido(p)
                .variante(v)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .build();

        detalleRepo.save(d);
        p.getDetalles().add(d);

        // recalcular total
        p.setTotal(p.getDetalles().stream()
                .mapToDouble(it -> it.getPrecioUnitario() * it.getCantidad())
                .sum());

        // si querés “documentar” en este momento:
        if (p.getEstado() == EstadoPedido.BORRADOR) p.setEstado(EstadoPedido.DOCUMENTADO);

        return p;
    }

    @Override
    @Transactional
    public Pedido confirmar(Long pedidoId) {
        Pedido p = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (p.getEstado() != EstadoPedido.DOCUMENTADO && p.getEstado() != EstadoPedido.BORRADOR) {
            throw new IllegalStateException("El pedido no está listo para confirmar");
        }

        // por cada línea: validar stock, descontar y registrar movimiento
        for (DetallePedido d : p.getDetalles()) {
            ProductoVariante v = varianteRepo.findById(d.getVariante().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

            if (v.getStockDisponible() < d.getCantidad()) {
                throw new IllegalStateException("Sin stock para SKU " + v.getSku());
            }

            v.setStockDisponible(v.getStockDisponible() - d.getCantidad());
            varianteRepo.save(v);

            movRepo.save(MovimientoStock.builder()
                    .variante(v)
                    .pedido(p)
                    .detalle(d)
                    .tipo(TipoMovimiento.BAJA_POR_PEDIDO)
                    .cantidad(d.getCantidad())
                    .fecha(LocalDateTime.now())
                    .build());
        }

        p.setEstado(EstadoPedido.CONFIRMADO);
        return pedidoRepo.save(p);
    }

    @Override
    @Transactional
    public Pedido cancelar(Long pedidoId) {
        Pedido p = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (p.getEstado() != EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("Sólo se cancelan pedidos confirmados");
        }

        for (DetallePedido d : p.getDetalles()) {
            ProductoVariante v = varianteRepo.findById(d.getVariante().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

            v.setStockDisponible(v.getStockDisponible() + d.getCantidad());
            varianteRepo.save(v);

            movRepo.save(MovimientoStock.builder()
                    .variante(v)
                    .pedido(p)
                    .detalle(d)
                    .tipo(TipoMovimiento.REVERSION_POR_ANULACION)
                    .cantidad(d.getCantidad())
                    .fecha(LocalDateTime.now())
                    .build());
        }

        p.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepo.save(p);
    }
}


