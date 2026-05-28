# Biblioteca API

Trabalho POO - microservico REST desenvolvido com Java e Spring Boot para gerenciamento de livros e categorias de uma biblioteca.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- PostgreSQL
- Springdoc OpenAPI / Swagger
- JUnit 5
- Mockito
- JaCoCo
- Maven

## Funcionalidades

- Cadastrar categorias
- Listar categorias
- Cadastrar livros
- Listar livros
- Buscar livro por ID
- Filtrar livros por autor ou categoria
- Atualizar livro
- Deletar livro
- Validacoes de entrada com DTOs
- Tratamento centralizado de erros

## Estrutura

```text
src/main/java/br/com/biblioteca
|-- config
|-- controller
|-- dto
|-- exception
|-- model
|-- repository
`-- service
```

## Como rodar localmente

### Pre-requisitos

- Java 17 instalado
- Maven instalado

### Executar a aplicacao

```bash
mvn spring-boot:run
```

A API ficara disponivel em:

```text
http://localhost:8080
```

## Banco H2

No profile `dev`, a aplicacao usa banco em memoria H2.

Console H2:

```text
http://localhost:8080/h2-console
```

Dados de acesso:

```text
JDBC URL: jdbc:h2:mem:biblioteca
Usuario: sa
Senha: deixe em branco
```

## Swagger

A documentacao interativa da API fica em:

```text
http://localhost:8080/swagger-ui.html
```

## Rotas principais

### Categorias

```text
POST /categorias
GET  /categorias
```

### Livros

```text
POST   /livros
GET    /livros
GET    /livros/{id}
GET    /livros/filtro?autor=machado
GET    /livros/filtro?categoria=romance
PUT    /livros/{id}
DELETE /livros/{id}
```

## Exemplos com cURL

### Criar categoria

```bash
curl -X POST http://localhost:8080/categorias \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Romance\",\"descricao\":\"Livros de romance\"}"
```

### Criar livro

```bash
curl -X POST http://localhost:8080/livros \
  -H "Content-Type: application/json" \
  -d "{\"titulo\":\"Dom Casmurro\",\"autor\":\"Machado de Assis\",\"isbn\":\"9788535910663\",\"anoPublicacao\":1899,\"disponivel\":true,\"categoriaId\":1}"
```

### Listar livros

```bash
curl http://localhost:8080/livros
```

### Buscar livro por ID

```bash
curl http://localhost:8080/livros/1
```

### Filtrar livros por autor

```bash
curl "http://localhost:8080/livros/filtro?autor=Machado"
```

### Atualizar livro

```bash
curl -X PUT http://localhost:8080/livros/1 \
  -H "Content-Type: application/json" \
  -d "{\"titulo\":\"Dom Casmurro\",\"autor\":\"Machado de Assis\",\"isbn\":\"9788535910663\",\"anoPublicacao\":1899,\"disponivel\":false,\"categoriaId\":1}"
```

### Deletar livro

```bash
curl -X DELETE http://localhost:8080/livros/1
```

## Testes e cobertura

Rodar testes:

```bash
mvn clean test
```

Gerar relatorio de cobertura com JaCoCo:

```bash
mvn clean verify
```

Relatorio:

```text
target/site/jacoco/index.html
```

## Profile de producao

No profile `prod`, a aplicacao usa variaveis de ambiente:

```properties
DATABASE_URL=jdbc:postgresql://host:5432/banco
DATABASE_USERNAME=usuario
DATABASE_PASSWORD=senha
SPRING_PROFILES_ACTIVE=prod
```

## Deploy em Producao

Sugestao:

- Aplicacao: Render
- Banco: PostgreSQL no Render ou Supabase

Passos gerais:

1. Criar o banco PostgreSQL.
2. Criar um Web Service apontando para o repositorio GitHub.
3. Configurar as variaveis `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` e `SPRING_PROFILES_ACTIVE=prod`.
4. Usar o comando de build `mvn clean package`.
5. Usar o comando de start `java -jar target/biblioteca-api-0.0.1-SNAPSHOT.jar`.

Link publico da API:

```text
Adicionar aqui o link depois do deploy.
```

## Divisao de tarefas

- Integrante 1: estrutura inicial, entidades e banco de dados
- Integrante 2: controllers e rotas REST
- Integrante 3: services e regras de negocio
- Integrante 4: testes unitarios e cobertura JaCoCo
- Integrante 5: Swagger, README e exemplos de uso
- Integrante 6: deploy e configuracao de producao
