package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Noticia;
import com.example.demo.Repository.NoticiaRepository;
import com.example.demo.dto.NoticiaDTO;

@Service
public class NoticiaServiceImpl implements NoticiaService {

    private final NoticiaRepository repository;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";  // Usar ruta relativa dentro de static
    private final String imagesDir = uploadDir + "images/";  // Directorio para imágenes
    private final String defaultImage = "/uploads/images/default.png";  // Imagen por defecto que debe existir en static

    public NoticiaServiceImpl(NoticiaRepository repository) {
        this.repository = repository;
        // Crear directorios si no existen (aún se puede dejar por si acaso)
        new File(uploadDir).mkdirs();
        new File(imagesDir).mkdirs();
    }

    @Override
    public NoticiaDTO guardarNoticia(String tituloInput, String contenidoInput, MultipartFile pdf, MultipartFile imagen) throws IOException {
        Noticia noticia = new Noticia();
        noticia.setFecha(LocalDateTime.now());

        // Para almacenar imágenes
        String imagenPath = defaultImage;  // Imagen por defecto si no hay imagen subida

        // Si hay una imagen subida, guardarla
        if (imagen != null && !imagen.isEmpty()) {
            String imagenFileName = Paths.get(imagen.getOriginalFilename()).getFileName().toString();
            File imagenFile = new File(imagesDir + imagenFileName);
            imagen.transferTo(imagenFile);  // Guardar la imagen en la carpeta correcta
            imagenPath = "/uploads/images/" + imagenFileName;  // Guardar la ruta relativa
        }

        // Guardar el PDF si se sube
        if (pdf != null && !pdf.isEmpty()) {
            String fileName = Paths.get(pdf.getOriginalFilename()).getFileName().toString();
            File pdfFile = new File(uploadDir + fileName);
            pdf.transferTo(pdfFile);  // Guardar el PDF en la carpeta correcta
            noticia.setPdfPath("/uploads/" + fileName);  // Ruta relativa para el PDF
        }

        // Procesar el PDF para extraer título y contenido
        if (pdf != null && !pdf.isEmpty()) {
            try (PDDocument document = PDDocument.load(new File(uploadDir + Paths.get(pdf.getOriginalFilename()).getFileName().toString()))) {
                String texto = new org.apache.pdfbox.text.PDFTextStripper().getText(document);
                String[] lineas = texto.split("\\r?\\n");

                // Título
                if (tituloInput == null || tituloInput.isEmpty()) {
                    noticia.setTitulo(lineas.length > 0 ? lineas[0].trim() : "Sin título");
                } else {
                    noticia.setTitulo(tituloInput);
                }

                // Contenido (desde la segunda línea)
                if (contenidoInput == null || contenidoInput.isEmpty()) {
                    if (lineas.length > 1) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < lineas.length; i++) {
                            sb.append(lineas[i].trim()).append("\n");
                        }
                        noticia.setContenido(sb.toString().trim());
                    } else {
                        noticia.setContenido("");
                    }
                } else {
                    noticia.setContenido(contenidoInput);
                }
            }
        }

        // Asignar imagen y contenido
        if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
            noticia.setTitulo("Sin título");
        }
        if (noticia.getContenido() == null) {
            noticia.setContenido("");
        }

        noticia.setImagen(imagenPath);

        // Guardar la noticia en la base de datos
        Noticia guardada = repository.save(noticia);

        // Crear DTO para devolver la noticia con la imagen
        NoticiaDTO dto = NoticiaDTO.fromEntity(guardada);
        dto.setImagen(imagenPath);  // Añadir la imagen al DTO

        return dto;
    }

    @Override
    public List<NoticiaDTO> listarNoticias() {
        return repository.findAll().stream().map(n -> {
            NoticiaDTO dto = NoticiaDTO.fromEntity(n);
            // Se asigna la imagen al DTO
            dto.setImagen(n.getImagen());
            return dto;
        }).toList();
    }
}