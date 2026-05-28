package br.com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "O nome da categoria e obrigatorio")
        @Size(max = 80, message = "O nome deve ter no maximo 80 caracteres")
        String nome,

        @NotBlank(message = "A descricao da categoria e obrigatoria")
        @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres")
        String descricao
) {
}
