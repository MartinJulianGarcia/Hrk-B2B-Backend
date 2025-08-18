package com.hrk.tienda_b2b.repository;


import com.hrk.tienda_b2b.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByClienteId(Long clienteId);


}