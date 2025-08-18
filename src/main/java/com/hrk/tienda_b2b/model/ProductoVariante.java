package com.hrk.tienda_b2b.model;


import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductoVariante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false, unique = true)
    private String sku;

    private String color;
    private String talle;

    // si querés precio por variante (recomendado)
    private Double precio;

    @Version
    private Long version; // para optimistic locking

    @Column(nullable = false)
    private Integer stockDisponible;

    public  Double getPrecio(){return precio;};

    public  Producto getProducto(){return producto;};

}