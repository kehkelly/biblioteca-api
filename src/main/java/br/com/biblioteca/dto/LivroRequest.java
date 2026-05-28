package br.com.biblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank(message = "O titulo e obrigatorio")
        @Size(max = 120, message = "O titulo deve ter no maximo 120 caracteres")
        String titulo,

        @NotBlank(message = "O autor e obrigatorio")
        @Size(max = 100, message = "O autor deve ter no maximo 100 caracteres")
        String autor,

        @NotBlank(message = "O ISBN e obrigatorio")
        @Size(max = 20, message = "O ISBN deve ter no maximo 20 caracteres")
        String isbn,

        @NotNull(message = "O ano de publicacao e obrigatorio")
        @Min(value = 1000, message = "O ano deve ser maior ou igual a 1000")
        @Max(value = 2100, message = "O ano deve ser menor ou igual a 2100")
        Integer anoPublicacao,

        @NotNull(message = "Informe se o livro esta disponivel")
        Boolean disponivel,

        @NotNull(message = "A categoria e obrigatoria")
        Long categoriaId
) {
}
