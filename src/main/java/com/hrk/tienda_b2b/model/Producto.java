package com.hrk.tienda_b2b.model;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.*;

import com.hrk.tienda_b2b.model.TipoProducto;


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

   //public  Double getPrecio(){return precio;};

    @Enumerated(EnumType.STRING)
    private TipoProducto tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;  // PLANO o TEJIDO

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoVariante> variantes = new ArrayList<>();
}