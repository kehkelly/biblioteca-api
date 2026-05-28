package br.com.biblioteca.dto;

import br.com.biblioteca.model.Livro;

public record LivroResponse(
        Long id,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        Boolean disponivel,
        CategoriaResponse categoria
) {
    public static LivroResponse fromEntity(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getDisponivel(),
                CategoriaResponse.fromEntity(livro.getCategoria())
        );
    }
}
