package br.com.biblioteca.service;

import br.com.biblioteca.dto.CategoriaRequest;
import br.com.biblioteca.dto.CategoriaResponse;
import br.com.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Romance", "Livros de romance");
        categoria.setId(1L);
    }

    @Test
    void deveCriarCategoria() {
        CategoriaRequest request = new CategoriaRequest("Romance", "Livros de romance");
        when(categoriaRepository.findByNomeIgnoreCase("Romance")).thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponse response = categoriaService.criar(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Romance");
    }

    @Test
    void naoDeveCriarCategoriaDuplicada() {
        CategoriaRequest request = new CategoriaRequest("Romance", "Livros de romance");
        when(categoriaRepository.findByNomeIgnoreCase("Romance")).thenReturn(Optional.of(categoria));

        assertThatThrownBy(() -> categoriaService.criar(request))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("categoria");
    }

    @Test
    void deveListarCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponse> response = categoriaService.listar();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).descricao()).isEqualTo("Livros de romance");
    }

    @Test
    void deveBuscarEntidadePorId() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria response = categoriaService.buscarEntidadePorId(1L);

        assertThat(response.getNome()).isEqualTo("Romance");
    }

    @Test
    void deveLancarErroQuandoCategoriaNaoExiste() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.buscarEntidadePorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }
}
