package br.com.biblioteca.controller;

import br.com.biblioteca.dto.CategoriaResponse;
import br.com.biblioteca.dto.LivroRequest;
import br.com.biblioteca.dto.LivroResponse;
import br.com.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService livroService;

    @Test
    void deveCriarLivro() throws Exception {
        LivroRequest request = new LivroRequest("Dom Casmurro", "Machado de Assis", "9788535910663", 1899, true, 1L);
        LivroResponse response = response();
        when(livroService.criar(any(LivroRequest.class))).thenReturn(response);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
    }

    @Test
    void deveRetornarErroQuandoRequestForInvalido() throws Exception {
        LivroRequest request = new LivroRequest("", "", "", 900, true, null);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Erro de validacao"));
    }

    @Test
    void deveListarLivros() throws Exception {
        when(livroService.listar()).thenReturn(List.of(response()));

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Machado de Assis"));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        when(livroService.buscarPorId(10L)).thenReturn(response());

        mockMvc.perform(get("/livros/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void deveRetornarNotFoundQuandoLivroNaoExiste() throws Exception {
        when(livroService.buscarPorId(99L)).thenThrow(new RecursoNaoEncontradoException("Livro nao encontrado com id 99"));

        mockMvc.perform(get("/livros/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detalhes[0]", containsString("99")));
    }

    @Test
    void deveFiltrarLivros() throws Exception {
        when(livroService.filtrar("Machado", null)).thenReturn(List.of(response()));

        mockMvc.perform(get("/livros/filtro").param("autor", "Machado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"));
    }

    @Test
    void deveAtualizarLivro() throws Exception {
        LivroRequest request = new LivroRequest("Dom Casmurro", "Machado de Assis", "9788535910663", 1899, true, 1L);
        when(livroService.atualizar(eq(10L), any(LivroRequest.class))).thenReturn(response());

        mockMvc.perform(put("/livros/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9788535910663"));
    }

    @Test
    void deveDeletarLivro() throws Exception {
        doNothing().when(livroService).deletar(10L);

        mockMvc.perform(delete("/livros/10"))
                .andExpect(status().isNoContent());
    }

    private LivroResponse response() {
        return new LivroResponse(
                10L,
                "Dom Casmurro",
                "Machado de Assis",
                "9788535910663",
                1899,
                true,
                new CategoriaResponse(1L, "Romance", "Livros de romance")
        );
    }
}
