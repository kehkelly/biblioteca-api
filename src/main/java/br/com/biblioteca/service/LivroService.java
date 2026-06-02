package br.com.biblioteca.service;

import br.com.biblioteca.dto.LivroRequest;
import br.com.biblioteca.dto.LivroResponse;
import br.com.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaService categoriaService;

    public LivroService(LivroRepository livroRepository, CategoriaService categoriaService) {
        this.livroRepository = livroRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public LivroResponse criar(LivroRequest request) {
        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new RegraNegocioException("Ja existe um livro cadastrado com esse ISBN");
        }

        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());
        Livro livro = new Livro(
                request.titulo(),
                request.autor(),
                request.isbn(),
                request.anoPublicacao(),
                request.disponivel(),
                categoria
        );

        return LivroResponse.fromEntity(livroRepository.save(livro));
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listar() {
        return livroRepository.findAll()
                .stream()
                .map(LivroResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        return LivroResponse.fromEntity(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> filtrar(String autor, String categoria) {
        if (autor != null && !autor.isBlank()) {
            return livroRepository.findByAutorContainingIgnoreCase(autor)
                    .stream()
                    .map(LivroResponse::fromEntity)
                    .toList();
        }

        if (categoria != null && !categoria.isBlank()) {
            return livroRepository.findByCategoriaNomeContainingIgnoreCase(categoria)
                    .stream()
                    .map(LivroResponse::fromEntity)
                    .toList();
        }

        return listar();
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request) {
        Livro livro = buscarEntidadePorId(id);

        livroRepository.findByIsbn(request.isbn()).ifPresent(livroComMesmoIsbn -> {
            if (!livroComMesmoIsbn.getId().equals(id)) {
                throw new RegraNegocioException("Ja existe outro livro cadastrado com esse ISBN");
            }
        });

        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());
        livro.setTitulo(request.titulo());
        livro.setAutor(request.autor());
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setDisponivel(request.disponivel());
        livro.setCategoria(categoria);

        return LivroResponse.fromEntity(livroRepository.save(livro));
    }

    @Transactional
    public void deletar(Long id) {
        Livro livro = buscarEntidadePorId(id);
        livroRepository.delete(livro);
    }

    private Livro buscarEntidadePorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro nao encontrado com id " + id));
    }
}
