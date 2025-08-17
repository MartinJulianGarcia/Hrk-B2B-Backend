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
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;       // precio base (opcional)
    private Integer stock;       // DEPRECADO si usás variantes
    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipo;

    @ManyToOne @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoVariante> variantes = new ArrayList<>();
}