package br.com.biblioteca.repository;

import br.com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutorContainingIgnoreCase(String autor);

    List<Livro> findByCategoriaNomeContainingIgnoreCase(String categoria);

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
