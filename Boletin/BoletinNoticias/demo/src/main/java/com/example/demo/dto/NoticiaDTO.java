package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Entity.Noticia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String contenido;
    private String pdfPath;
    private LocalDateTime fecha;
    private String imagen;  // Cambiado de List<String> a String para una sola imagen

    public static NoticiaDTO fromEntity(Noticia noticia) {
        if (noticia == null) return null;
        NoticiaDTO dto = NoticiaDTO.builder()
                .id(noticia.getId())
                .titulo(noticia.getTitulo())
                .contenido(noticia.getContenido())
                .pdfPath(noticia.getPdfPath())
                .fecha(noticia.getFecha())
                .build();
        
        // Asignamos la imagen en lugar de una lista de imágenes
        dto.setImagen(noticia.getImagen());  // Aquí se asigna la imagen directamente
        return dto;
    }
}