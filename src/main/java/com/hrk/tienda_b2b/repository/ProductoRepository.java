package com.hrk.tienda_b2b.repository;

import com.hrk.tienda_b2b.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Al extender de JpaRepository no hace falta implementar esta interface, el frame lo hace solo
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // agregar métodos personalizados después, por ejemplo:
    // List<Producto> findByCategoria(Categoria categoria);

    //la extension de JPA Ya implementa devolver todos, buscar por id etc
}