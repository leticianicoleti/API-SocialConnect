# Registro de Uso de IA Generativa

> **Política da disciplina:** O uso de IA generativa é permitido como assistente.
> O aluno continua responsável por testar, revisar e explicar o código entregue.

## Declaração de Uso de IA (A1)

**Avaliação:** A1 — Módulo de Produtos | ITE005 — Tópicos Especiais em Sistemas para Internet III
**Projeto:** SocialConnect API | **Branch:** `avaliacao1`

### Ferramentas utilizadas

- [x] ChatGPT / Claude / Gemini (ChatGPT / Codex)
- [ ] Copilot / Codeium
- [ ] Nenhuma

### Como utilizei

- Usei IA para interpretar os requisitos obrigatórios e o bônus do arquivo `avaliacao1.html`.
- Usei IA para tirar dúvidas sobre as regras do Service (estoque não negativo, estoque baixo e nome único).
- Usei IA para tirar dúvidas sobre a semântica HTTP do Controller e o uso de Problem Details (RFC 7807).
- Usei IA para revisar as anotações do Swagger/OpenAPI (`@Tag`, `@Operation`, `@ApiResponse`, `@Schema`).
- Usei IA para entender erros de compilação e mensagens dos testes.
- Usei IA para revisar meus testes unitários (Mockito) e de integração (Testcontainers).

### O que foi implementado ou revisado com apoio da IA

- Entity `Produto`, enum `CategoriaProduto` e migration Flyway com chave primária `id_produto`.
- DTOs como `record` com Bean Validation e validação customizada para unidade de medida.
- Regras do Service: estoque não negativo (422), campo `estoqueBaixo` e nome único (409).
- Tratamento de erros com Problem Details para nome duplicado e estoque negativo.
- Testes unitários com Mockito (padrão AAA).
- Testes de integração com Testcontainers (PostgreSQL 17).

### O que eu entendo 

- A Entity representa a tabela do banco de dados.
- O DTO de request recebe os dados enviados pelo usuário ou pelo Swagger.
- O DTO de response devolve os dados tratados pela API.
- O campo `estoqueBaixo` fica `true` quando `estoqueAtual` é menor que `estoqueMinimo`.
- A lógica de validação de estoque negativo (422) e de nome duplicado (409) no Service.
- O Controller recebe as requisições HTTP (GET, POST, PUT e DELETE) e devolve os status corretos (200, 201 com `Location`, 204, 404).
- O Service concentra as regras de negócio.
- O Repository faz a comunicação com o banco usando Spring Data JPA.
- As migrations do Flyway criam e atualizam as tabelas do banco.
- O Swagger permite testar os endpoints pelo navegador em `http://localhost:8080/swagger-ui.html`.
- O Lombok gera automaticamente métodos repetitivos, como getters, setters, construtores e builders.
- O padrão AAA (Arrange, Act, Assert) nos testes unitários.
- A injeção de dependência via construtor com campos `final`.

### O que precisei estudar mais

- Como o Lombok funciona durante a compilação por annotation processing.
- Como configurar testes com Testcontainers.
- Como funciona o retorno `422 Unprocessable Entity` e por que ele difere do `400`.
- Como criar uma validação customizada com Jakarta Validation.
- Como alinhar a senha do PostgreSQL entre `docker-compose.yml` e `application.properties`.

## Registro de Prompts

| # | Data | Requisito da A1 | Ferramenta | Prompt (resumo) | Uso da saída |
|---|------|-----------------|------------|-----------------|--------------|
| 1 | 02/10/2026 | Entidade e Persistência (15%) | ChatGPT / Codex | "Ajude a escrever a migration Flyway V4__create_produtos_table.sql com id_produto BIGINT auto-incremento, nome VARCHAR(150) NOT NULL UNIQUE, categoria VARCHAR(20), estoques INT >= 0, unidade_medida VARCHAR(20) e data_cadastro DATE." | Usei a resposta como base e validei a migration. |
| 2 | 02/10/2026 | DTOs e Validações (15%) | ChatGPT / Codex | "Ajude a criar ProdutoRequestDTO e ProdutoResponseDTO como record, usando @NotBlank, @Size, @NotNull e @Min com mensagens do messages.properties." | Usei a estrutura sugerida e ajustei ao projeto. |
| 3 | 02/10/2026 | DTOs e Validações (15%) | ChatGPT / Codex | "Ajudar a criar uma validação customizada com Jakarta Validation, usando anotação e ConstraintValidator, para a unidade de medida do produto?" | Usei a explicação como referência para implementar a validação e entender o ciclo de validação. |
| 4 | 02/10/2026 | Service e Regras (20%) | ChatGPT / Codex | "Ajude a retornar 409 quando o nome do produto já existir e 422 quando a operação resultar em estoque negativo, usando Problem Details." | Usei a orientação para implementar as exceções de negócio e o tratamento global de erros. |
| 5 | 02/10/2026 | Service e Regras (20%) | ChatGPT / Codex | "Ajude a entender por que estoque negativo deve retornar 422 e não 400?" | Usei a explicação para entender a diferença entre erro de formato e regra de negócio. |
| 6 | 02/10/2026 | Service e Regras (20%) | ChatGPT / Codex | "Explique como calcular o campo estoqueBaixo no ProdutoResponseDTO, considerando true quando estoqueAtual < estoqueMinimo." | Usei a explicação para revisar a lógica de comparação entre estoque atual e estoque mínimo. |
| 7 | 02/10/2026 | Service e Regras (20%) | ChatGPT / Codex | "Ajude a implementar a interface ProdutoService com listar, filtros por nome e categoria, Pageable, buscarPorId, criar, atualizar e deletar." | Usei o código como ponto de partida, revisei e entendi cada método antes de mantê-lo. |
| 8 | 02/10/2026 | Controller e HTTP (20%) | ChatGPT / Codex | "Ajude a criar o ProdutoController em /api/v1/produtos com GET, POST 201 com Location, PUT 200 e DELETE 204, usando injeção por construtor." | Usei a orientação para compreender a semântica HTTP e conferir o status de cada endpoint. |
| 9 | 02/10/2026 | Swagger/OpenAPI (15%) | ChatGPT / Codex | "Ajude a entender como documentar todos os endpoints com @Tag, @Operation e @ApiResponse e adicionar exemplos nos DTOs com @Schema(example)." | Usei a ajuda para completar a documentação e validar o Swagger UI navegável. |
| 10 | 02/10/2026 | Swagger/OpenAPI (15%) | ChatGPT / Codex | "Ajude a entender como testar os endpoints de produtos pelo Swagger." | Usei a ajuda para montar exemplos de requisição e validar o funcionamento da API. |
| 11 | 02/10/2026 | Qualidade do Código (15%) | ChatGPT / Codex | "Ajude a revisar como organizar os pacotes controller, service, repository, dto e model e como escrever commits seguindo Conventional Commits." | Usei a ajuda para padronizar a estrutura e as mensagens dos commits. |
| 12 | 02/10/2026 | Bônus - Testes unitários (+10%) | ChatGPT / Codex | "Ajude a entender como criar ProdutoServiceTest com Mockito, padrão AAA e os cenários de criação válida, estoque negativo e nome duplicado." | Usei a ajuda como base e ajustei os mocks e as asserções ao meu Service. |
| 13 | 02/10/2026 | Bônus - Testes de integração (+15%) | ChatGPT / Codex | "Ajude a entender como criar ProdutoControllerIntegrationTest com SpringBootTest, Testcontainers e PostgreSQL 17, cobrindo 201, 409 e 422." | Usei a ajuda como base e estudei a configuração do container e do @DynamicPropertySource. |
| 14 | 02/10/2026 | Build e testes | ChatGPT / Codex | "Deu erro no teste ou na compilação do Maven; pedi ajuda para entender o problema e corrigir a execução." | Usei a ajuda para identificar como interpretar o erro e ajustar o código. |

---
