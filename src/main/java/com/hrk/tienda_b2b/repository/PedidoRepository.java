package com.hrk.tienda_b2b.repository;
import com.hrk.tienda_b2b.model.Pedido;


import com.hrk.tienda_b2b.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> { }