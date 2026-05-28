package br.com.biblioteca.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        List<String> detalhes
) {
    public static ErroResponse of(int status, String erro, List<String> detalhes) {
        return new ErroResponse(LocalDateTime.now(), status, erro, detalhes);
    }
}
