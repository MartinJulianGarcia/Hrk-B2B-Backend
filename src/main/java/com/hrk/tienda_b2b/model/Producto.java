package com.hrk.tienda_b2b.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}