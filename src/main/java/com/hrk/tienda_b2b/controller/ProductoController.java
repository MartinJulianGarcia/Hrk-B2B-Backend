package com.hrk.tienda_b2b.controller;

import com.hrk.tienda_b2b.dto.CreateProductoRequest;
import com.hrk.tienda_b2b.model.Producto;
import com.hrk.tienda_b2b.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody CreateProductoRequest request) {
        try {
            System.out.println("🔵 [BACKEND] Request recibido: " + request);
            System.out.println("🔵 [BACKEND] Nombre: " + request.getNombre());
            System.out.println("🔵 [BACKEND] Tipo: " + request.getTipo());
            System.out.println("🔵 [BACKEND] Categoría: " + request.getCategoria());
            System.out.println("🔵 [BACKEND] SKU: " + request.getSku());
            System.out.println("🔵 [BACKEND] Colores: " + request.getColores());
            System.out.println("🔵 [BACKEND] Talles: " + request.getTalles());
            System.out.println("🔵 [BACKEND] Precio: " + request.getPrecio());
            System.out.println("🔵 [BACKEND] Stock: " + request.getStock());
            System.out.println("🔵 [BACKEND] Descripción: " + request.getDescripcion());
            System.out.println("🔵 [BACKEND] Imagen URL: " + request.getImagenUrl());
            
            // Validar datos básicos
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                System.out.println("🔴 [BACKEND] Error: Nombre vacío");
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getTipo() == null) {
                System.out.println("🔴 [BACKEND] Error: Tipo vacío");
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getCategoria() == null) {
                System.out.println("🔴 [BACKEND] Error: Categoría vacía");
                return ResponseEntity.badRequest().build();
            }
            
            Producto nuevo = productoService.crearProducto(request);
            System.out.println("✅ [BACKEND] Producto creado exitosamente: " + nuevo.getId());
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            System.out.println("🔴 [BACKEND] Error al crear producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}