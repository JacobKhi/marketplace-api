package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.model.Categoria;
import br.com.iff.marketplace.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    // Endpoint para CADASTRAR uma nova categoria
    @PostMapping
    public ResponseEntity<Categoria> cadastrarCategoria(@RequestBody Categoria categoria) {
        Categoria novaCategoria = service.salvarCategoria(categoria);
        return ResponseEntity.ok(novaCategoria);
    }

    // Endpoint para LISTAR todas as categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = service.listarCategorias();
        return ResponseEntity.ok(categorias);
    }
}