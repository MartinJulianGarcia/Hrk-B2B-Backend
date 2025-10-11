package com.hrk.tienda_b2b.repository;
import java.util.List;
import com.hrk.tienda_b2b.model.Producto;

import com.hrk.tienda_b2b.model.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Al extender de JpaRepository no hace falta implementar esta interface, el frame lo hace solo
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByTipo(TipoProducto tipo);  // ✅ Correcto usar enum
    List<Producto> findByCategoriaAndTipo(String categoria, TipoProducto tipo);
}