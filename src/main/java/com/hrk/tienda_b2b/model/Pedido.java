package com.hrk.tienda_b2b.model;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity @Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipo;   // VENTA | DEVOLUCION

    // opcional: link al pedido original
    private Long pedidoOrigenId;  // si la devolución refiere a una venta previa

    private Double total;

    // si ya tenés Usuario luego lo reemplazás; por ahora guardamos el id del cliente
    private Long clienteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

   public List<DetallePedido> getDetalles(){return detalles;}

    public Pedido(Long cl, double tot,LocalDateTime f,EstadoPedido est)
    {
        this.clienteId=cl;
        this.total=tot;
        this.estado=est;
        this.fecha=f;

    }

    public void setTotal( Double total)
    {
       this.total=total;
    }
    //Pedido p = Pedido.builder()
      //      .clienteId(c.getClienteId())
       //     .fecha(LocalDateTime.now())
         //   .estado(EstadoPedido.DOCUMENTADO)  // o BORRADOR si querés
          //  .total(0.0)
          //  .build();
     //   pedidoRepo.save(p);
}