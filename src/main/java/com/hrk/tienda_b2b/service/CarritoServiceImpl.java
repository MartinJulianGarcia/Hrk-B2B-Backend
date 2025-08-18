

package com.hrk.tienda_b2b.service;



import com.hrk.tienda_b2b.model.*;
import com.hrk.tienda_b2b.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepo;
    private final CarritoItemRepository itemRepo;
    private final ProductoVarianteRepository varianteRepo;
    private final PedidoRepository pedidoRepo;
    private final DetallePedidoRepository detalleRepo;

    //public CarritoServiceImpl(CarritoRepository carritoRepo, CarritoItemRepository itemRepo, ProductoVarianteRepository varianteRepo, PedidoRepository pedidoRepo, DetallePedidoRepository detalleRepo) {
      //  this.carritoRepo = carritoRepo;
     //   this.itemRepo = itemRepo;
      //  this.varianteRepo = varianteRepo;
     //   this.pedidoRepo = pedidoRepo;
     //   this.detalleRepo = detalleRepo;
    //}

    @Override
    @Transactional
    public Long crearCarrito(Long clienteId) {
        Carrito c = new Carrito(clienteId, LocalDateTime.now());

        return carritoRepo.save(c).getId();
    }

    @Override
    @Transactional
    public void agregarItem(Long carritoId, Long varianteId, int cantidad) {
        Carrito c = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        ProductoVariante v = varianteRepo.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        Double precioSugerido = (v.getPrecio() != null) ? v.getPrecio()
                : (v.getProducto().getPrecio() != null ? v.getProducto().getPrecio() : 0.0);
//   public CarritoItem(Carrito c, ProductoVariante v, Integer can) {
//        this.carrito = c;
//        this.variante=v;
//        this.cantidad=can;
//    }
        CarritoItem item = new CarritoItem(c,v,cantidad);


        itemRepo.save(item);
        c.getItems().add(item);
    }

    @Override
    @Transactional
    public void quitarItem(Long carritoItemId) {
        itemRepo.deleteById(carritoItemId);
    }

    @Override
    @Transactional
    public void vaciar(Long carritoId) {
        Carrito c = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        c.getItems().clear(); // orphanRemoval=true → borra en BD
    }

    @Override
    @Transactional
    public Pedido convertirACreatePedido(Long carritoId) {
        Carrito c = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        //Pedido p = Pedido.builder()
              //  .clienteId(c.getClienteId())
              //  .fecha(LocalDateTime.now())
               // .estado(EstadoPedido.DOCUMENTADO)  // o BORRADOR si querés
               // .total(0.0)
              //  .build();
        Pedido p = new Pedido (c.getId(),0.0,LocalDateTime.now(),EstadoPedido.DOCUMENTADO);
        pedidoRepo.save(p);

        double total = 0.0;
        for (CarritoItem ci : c.getItems()) {
            Double precio = (ci.getPrecioUnitarioSugerido() != null)
                    ? ci.getPrecioUnitarioSugerido()
                    : (ci.getVariante().getPrecio() != null ? ci.getVariante().getPrecio()
                    : (ci.getVariante().getProducto().getPrecio() != null ? ci.getVariante().getProducto().getPrecio() : 0.0));

            DetallePedido dp = new DetallePedido(p,ci.getVariante(),ci.getCantidad(),precio);

                 //   DetallePedido.builder()
                 //   .pedido(p)
                 //   .variante(ci.getVariante())
                  //  .cantidad(ci.getCantidad())
                 //   .precioUnitario(precio) // se “congela” el precio
                  //  .build();
            detalleRepo.save(dp);
            p.getDetalles().add(dp);

            total += precio * ci.getCantidad();
        }

        p.setTotal(total);
        pedidoRepo.save(p);

        // si querés, vaciá el carrito después de crear el pedido:
        c.getItems().clear();

        return p;
    }
}


