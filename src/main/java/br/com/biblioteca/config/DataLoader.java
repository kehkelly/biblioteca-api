package br.com.biblioteca.config;

import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.CategoriaRepository;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DataLoader {

    @Bean
    CommandLineRunner carregarDadosIniciais(CategoriaRepository categoriaRepository, LivroRepository livroRepository) {
        return args -> {
            if (categoriaRepository.count() > 0) {
                return;
            }

            Categoria romance = categoriaRepository.save(new Categoria("Romance", "Obras literarias de romance"));
            Categoria tecnologia = categoriaRepository.save(new Categoria("Tecnologia", "Livros tecnicos e de tecnologia"));

            livroRepository.save(new Livro("Dom Casmurro", "Machado de Assis", "9788535910663", 1899, true, romance));
            livroRepository.save(new Livro("Clean Code", "Robert C. Martin", "9780132350884", 2008, true, tecnologia));
        };
    }
}
