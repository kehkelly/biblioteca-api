# Biblioteca API

Trabalho POO - microserviço REST desenvolvido com Java e Spring Boot para gerenciamento de livros e categorias de uma biblioteca.

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

### Pré-requisitos

- Java 17 instalado
- Maven instalado

### Executar a aplicacao

```bash
mvn spring-boot:run
```

A API ficará disponivel em:

```text
http://localhost:8080
```

## Banco H2

No profile `dev`, a aplicação usa banco em memória H2.

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

A documentação interativa da API fica em:

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

Gerar relatório de cobertura com JaCoCo:

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

## Deploy em Produção

Link público da API:

```text
https://biblioteca-api-zk6g.onrender.com
```

API publicada no Render e disponível publicamente.

O deploy foi planejado para a plataforma Render, utilizando Docker para conteinerizar a aplicação Spring Boot e PostgreSQL como banco de dados de produção. Para isso, foram criados os arquivos `Dockerfile` e `.dockerignore`.

### Plataforma utilizada

- Aplicação: Render Web Service
- Banco de dados: PostgreSQL no Render ou Supabase
- Estratégia: aplicação conteinerizada com Docker

Passos gerais:

1. Criar um banco PostgreSQL no Render ou Supabase.
2. Criar um novo Web Service no Render apontando para o repositório GitHub do projeto.
3. Escolher a opção de deploy via Docker, usando o `Dockerfile` presente na raiz do projeto.
4. Configurar as variáveis de ambiente no painel do Render.
5. Fazer o deploy e validar a API pelo Swagger.

### Variáveis de ambiente

```properties
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://host:5432/nome_do_banco
DATABASE_USERNAME=usuario_do_banco
DATABASE_PASSWORD=senha_do_banco
```

O profile `prod` usa a porta fornecida pela plataforma por meio da variável `PORT`. Caso essa variável não exista, a aplicação usa a porta `8080`.

### Validação em produção

Após o deploy, acessar:

```text
https://biblioteca-api-zk6g.onrender.com/swagger-ui.html
```

Também é possível testar uma rota pública da API:

```bash
curl https://biblioteca-api-zk6g.onrender.com/livros
```

## Divisao de tarefas

- Kelly Dantas: estrutura inicial, entidades e banco de dados
- Rubens Bolognesi: controllers e rotas REST
- Kelly Dantas: services e regras de negocio
- Rubens Bolognesi: testes unitarios e cobertura JaCoCo
- Kelly Dantas: Swagger, README e exemplos de uso
- Rubens Bolognesi: deploy e configuracao de producao
