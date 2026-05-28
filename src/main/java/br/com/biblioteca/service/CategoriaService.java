package br.com.biblioteca.service;

import br.com.biblioteca.dto.CategoriaRequest;
import br.com.biblioteca.dto.CategoriaResponse;
import br.com.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponse criar(CategoriaRequest request) {
        categoriaRepository.findByNomeIgnoreCase(request.nome()).ifPresent(categoria -> {
            throw new RegraNegocioException("Ja existe uma categoria com esse nome");
        });

        Categoria categoria = new Categoria(request.nome(), request.descricao());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaResponse.fromEntity(categoriaSalva);
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::fromEntity)
                .toList();
    }

    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria nao encontrada com id " + id));
    }
}
