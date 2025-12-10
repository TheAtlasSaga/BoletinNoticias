package com.example.demo.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.NoticiaService;
import com.example.demo.dto.NoticiaDTO;

@RestController
@RequestMapping("/api/noticias")
@CrossOrigin(origins = "*") 
public class NoticiaController {

    private final NoticiaService service;

    public NoticiaController(NoticiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<NoticiaDTO> listarNoticias() {
        System.out.println("Recibiendo solicitud GET para listar noticias");
        return service.listarNoticias();
    }

    @PostMapping("/agregar")
    public NoticiaDTO agregarNoticia(
            @RequestParam String titulo,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) MultipartFile imagen,
            @RequestParam(required = false) MultipartFile pdf
    ) throws IOException {
        System.out.println("Recibiendo solicitud POST para agregar noticia");
        System.out.println("Título: " + titulo);
        System.out.println("Contenido: " + contenido);
        if (imagen != null) {
            System.out.println("Imagen: " + imagen.getOriginalFilename());
        }
        if (pdf != null) {
            System.out.println("PDF: " + pdf.getOriginalFilename());
        }
        return service.guardarNoticia(titulo, contenido, pdf, imagen);
    }
}