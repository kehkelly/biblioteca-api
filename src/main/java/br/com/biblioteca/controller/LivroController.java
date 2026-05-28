package br.com.biblioteca.controller;

import br.com.biblioteca.dto.LivroRequest;
import br.com.biblioteca.dto.LivroResponse;
import br.com.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Gerenciamento dos livros da biblioteca")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @Operation(summary = "Criar livro", description = "Cadastra um novo livro")
    public ResponseEntity<LivroResponse> criar(@Valid @RequestBody LivroRequest request) {
        LivroResponse response = livroService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Lista todos os livros cadastrados")
    public ResponseEntity<List<LivroResponse>> listar() {
        return ResponseEntity.ok(livroService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Busca um livro especifico pelo identificador")
    public ResponseEntity<LivroResponse> buscarPorId(
            @Parameter(description = "ID do livro") @PathVariable Long id
    ) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping("/filtro")
    @Operation(summary = "Filtrar livros", description = "Filtra livros por autor ou categoria")
    public ResponseEntity<List<LivroResponse>> filtrar(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria
    ) {
        return ResponseEntity.ok(livroService.filtrar(autor, categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza todos os dados de um livro existente")
    public ResponseEntity<LivroResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequest request
    ) {
        return ResponseEntity.ok(livroService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar livro", description = "Remove um livro pelo identificador")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
