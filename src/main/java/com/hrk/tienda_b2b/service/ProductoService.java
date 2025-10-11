package com.hrk.tienda_b2b.service;

import com.hrk.tienda_b2b.dto.CreateProductoRequest;
import com.hrk.tienda_b2b.model.Categoria;
import com.hrk.tienda_b2b.model.Producto;
import com.hrk.tienda_b2b.model.ProductoVariante;
import com.hrk.tienda_b2b.model.TipoProducto;
import com.hrk.tienda_b2b.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> obtenerPorTipo(TipoProducto tipo) {  // ✅ Enum como parámetro
        return productoRepository.findByTipo(tipo);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        // Validar que tenga imagen o asignar default
        if (producto.getImagenUrl() == null || producto.getImagenUrl().isEmpty()) {
            producto.setImagenUrl(producto.getTipo().getImagenDefault()); // ✅ Ahora funciona
        }
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional
    public Producto crearProducto(CreateProductoRequest request) {
        // 1. Crear el producto base
        Producto producto = Producto.builder()
            .nombre(request.getNombre())
            .tipo(request.getTipo())
            .categoria(request.getCategoria())
            .descripcion(request.getDescripcion())
            .precio(request.getPrecio()) // Precio base
            .stock(request.getStock()) // Stock total
            .build();
        
        // 2. Manejar imagen: usar la proporcionada o la por defecto
        if (request.getImagenUrl() != null && !request.getImagenUrl().isEmpty()) {
            producto.setImagenUrl(request.getImagenUrl());
        } else {
            producto.setImagenUrl(request.getTipo().getImagenDefault());
        }
        
        // 3. Guardar el producto primero
        producto = guardar(producto);
        
        // 4. Crear las variantes para cada combinación de color y talle
        for (String color : request.getColores()) {
            for (String talle : request.getTalles()) {
                ProductoVariante variante = ProductoVariante.builder()
                    .producto(producto)
                    .sku(generarSku(request.getSku(), color, talle))
                    .color(color)
                    .talle(talle)
                    .precio(request.getPrecio()) // Mismo precio para todas las variantes
                    .stockDisponible(request.getStock() / (request.getColores().size() * request.getTalles().size())) // Distribuir stock
                    .build();
                
                producto.getVariantes().add(variante);
            }
        }
        
        // 5. Guardar el producto con las variantes
        return guardar(producto);
    }
    
    private String generarSku(String skuBase, String color, String talle) {
        // Generar SKU único para cada variante
        return String.format("%s-%s-%s", skuBase, color.substring(0, 2).toUpperCase(), talle.toUpperCase());
    }
}