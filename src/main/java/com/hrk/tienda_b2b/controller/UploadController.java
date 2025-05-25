package com.hrk.tienda_b2b.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping
    public ResponseEntity<String> subirImagen(@RequestParam("file") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío");
        }

        try {
            // Crear la carpeta si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Limpiar y obtener nombre del archivo
            String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());

            // Ruta destino
            Path archivoPath = uploadPath.resolve(nombreArchivo);

            // Guardar el archivo
            Files.copy(archivo.getInputStream(), archivoPath, StandardCopyOption.REPLACE_EXISTING);

            // Devolver la URL para guardar en imagenUrl
            String url = "http://localhost:8081/uploads/" + nombreArchivo;
            return ResponseEntity.ok(url);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir archivo");
        }
    }
}