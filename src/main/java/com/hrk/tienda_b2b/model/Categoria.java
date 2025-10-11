package com.hrk.tienda_b2b.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity


@Table(name = "categorias")
@Getter @Setter     // esto evita tener que hacer un gettter y un setter en cada atributo
@NoArgsConstructor @AllArgsConstructor   // genera por si solo un constructo vacio y uno con todos los campos
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producto> productos;

    public void setId(Long id) {
        this.id = id;
    }
}