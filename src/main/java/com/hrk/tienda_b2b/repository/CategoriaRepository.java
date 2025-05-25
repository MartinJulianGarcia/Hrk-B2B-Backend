package com.hrk.tienda_b2b.repository;

import com.hrk.tienda_b2b.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}