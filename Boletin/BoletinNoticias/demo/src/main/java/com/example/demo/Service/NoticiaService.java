package com.example.demo.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NoticiaDTO;

public interface NoticiaService {

    // Método para guardar una noticia con su imagen y PDF
    NoticiaDTO guardarNoticia(String titulo, String contenido, MultipartFile pdf, MultipartFile imagen) throws IOException;

    // Método para listar todas las noticias
    List<NoticiaDTO> listarNoticias();
}
