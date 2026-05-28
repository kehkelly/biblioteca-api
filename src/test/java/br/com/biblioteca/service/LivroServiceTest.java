package br.com.biblioteca.service;

import br.com.biblioteca.dto.LivroRequest;
import br.com.biblioteca.dto.LivroResponse;
import br.com.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private LivroService livroService;

    private Categoria categoria;
    private Livro livro;
    private LivroRequest request;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Romance", "Livros de romance");
        categoria.setId(1L);

        livro = new Livro("Dom Casmurro", "Machado de Assis", "9788535910663", 1899, true, categoria);
        livro.setId(10L);

        request = new LivroRequest("Dom Casmurro", "Machado de Assis", "9788535910663", 1899, true, 1L);
    }

    @Test
    void deveCriarLivroComSucesso() {
        when(livroRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(categoriaService.buscarEntidadePorId(1L)).thenReturn(categoria);
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        LivroResponse response = livroService.criar(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.titulo()).isEqualTo("Dom Casmurro");
        assertThat(response.categoria().nome()).isEqualTo("Romance");
    }

    @Test
    void naoDeveCriarLivroComIsbnDuplicado() {
        when(livroRepository.existsByIsbn(request.isbn())).thenReturn(true);

        assertThatThrownBy(() -> livroService.criar(request))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("ISBN");
    }

    @Test
    void deveListarLivros() {
        when(livroRepository.findAll()).thenReturn(List.of(livro));

        List<LivroResponse> livros = livroService.listar();

        assertThat(livros).hasSize(1);
        assertThat(livros.get(0).autor()).isEqualTo("Machado de Assis");
    }

    @Test
    void deveBuscarLivroPorId() {
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));

        LivroResponse response = livroService.buscarPorId(10L);

        assertThat(response.id()).isEqualTo(10L);
    }

    @Test
    void deveLancarErroQuandoLivroNaoExiste() {
        when(livroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deveFiltrarPorAutor() {
        when(livroRepository.findByAutorContainingIgnoreCase("Machado")).thenReturn(List.of(livro));

        List<LivroResponse> response = livroService.filtrar("Machado", null);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).titulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    void deveAtualizarLivro() {
        LivroRequest novoRequest = new LivroRequest("Memorias Postumas", "Machado de Assis", "1111111111", 1881, false, 1L);
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));
        when(livroRepository.findByIsbn("1111111111")).thenReturn(Optional.empty());
        when(categoriaService.buscarEntidadePorId(1L)).thenReturn(categoria);
        when(livroRepository.save(livro)).thenReturn(livro);

        LivroResponse response = livroService.atualizar(10L, novoRequest);

        assertThat(response.titulo()).isEqualTo("Memorias Postumas");
        assertThat(response.disponivel()).isFalse();
    }

    @Test
    void deveDeletarLivro() {
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));

        livroService.deletar(10L);

        verify(livroRepository).delete(livro);
    }
}
