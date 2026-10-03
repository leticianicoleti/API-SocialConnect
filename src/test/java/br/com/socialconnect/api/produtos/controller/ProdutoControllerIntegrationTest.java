package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ProdutoControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configurarDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar produto quando dados válidos")
    void deveCriarProdutoQuandoDadosValidos() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<ProdutoResponseDTO> response = restTemplate.postForEntity(
                "/api/v1/produtos",
                dto,
                ProdutoResponseDTO.class
        );

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().idProduto());
        assertTrue(response.getBody().estoqueBaixo());
    }

    @Test
    @DisplayName("Deve retornar 409 quando nome duplicado")
    void deveRetornar409QuandoNomeDuplicado() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );
        restTemplate.postForEntity("/api/v1/produtos", dto, ProdutoResponseDTO.class);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/produtos",
                dto,
                String.class
        );

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Produto já cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar 422 quando estoque negativo")
    void deveRetornar422QuandoEstoqueNegativo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Feijão 1kg",
                CategoriaProduto.ALIMENTO,
                -1,
                5,
                "unidade"
        );

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/produtos",
                dto,
                String.class
        );

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Estoque atual não pode ser negativo"));
    }
}
