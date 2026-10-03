# SocialConnect API

API RESTful para gestão de instituições sociais.

Projeto desenvolvido com Java 21, Spring Boot, Spring Data JPA, Flyway, PostgreSQL e Lombok.

## Como Rodar

### Pré-requisitos

- JDK 21
- Maven 3.9+ ou Maven Wrapper
- PostgreSQL configurado
- Docker Desktop, se optar pelo `docker-compose.yml`

### Banco de Dados

O projeto usa PostgreSQL. As credenciais configuradas são:

- Host: `localhost:5432`
- Database: `socialconnect`
- User: `postgres`
- Password: `123456`

Para iniciar o PostgreSQL com Docker:

```bash
docker compose up -d
```

### Rodar a Aplicação

No Linux/macOS:

```bash
./mvnw clean compile
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

Swagger:

`http://localhost:8080/swagger-ui.html`

## Módulo de Produtos

Endpoints principais:

- `GET /api/v1/produtos`
- `GET /api/v1/produtos/{idProduto}`
- `POST /api/v1/produtos`
- `PUT /api/v1/produtos/{idProduto}`
- `DELETE /api/v1/produtos/{idProduto}`

Exemplo de criação de produto:

```json
{
  "nome": "Arroz 5kg",
  "categoria": "ALIMENTO",
  "estoqueAtual": 3,
  "estoqueMinimo": 10,
  "unidadeMedida": "unidade"
}
```

Regras implementadas:

- Nome do produto deve ser único.
- Estoque atual não pode ser negativo.
- Estoque negativo retorna `422 Unprocessable Entity`.
- Nome duplicado retorna `409 Conflict`.
- A resposta possui o campo `estoqueBaixo`.
- `estoqueBaixo` fica `true` quando `estoqueAtual < estoqueMinimo`.
- A listagem permite filtros opcionais por `nome` e `categoria`.
- A listagem possui paginação e ordenação.

## Outros Módulos

O projeto também possui endpoints para:

- Beneficiários: `/api/v1/beneficiarios`
- Doadores: `/api/v1/doadores`
- Doações: `/api/v1/doacoes`

## Testes

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

Foram criados testes para o módulo de Produtos, incluindo:

- Testes unitários do `ProdutoService`.
- Testes de integração dos endpoints de produtos.
- Cenários de criação válida, nome duplicado, estoque negativo e filtros.
- Testes com Testcontainers e PostgreSQL.

Os testes de integração com Testcontainers precisam do Docker em execução.

## Compatibilidade

O projeto exige JDK 21. O Lombok está configurado no `pom.xml` como annotation processor para a compilação da aplicação e dos testes.

Confira a versão instalada com:

```bash
java -version
javac -version
```

## Uso de IA

A declaração de uso de IA está documentada no arquivo:

`AI_USAGE.md`
