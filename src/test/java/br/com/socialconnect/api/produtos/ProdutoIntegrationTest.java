package br.com.socialconnect.api.produtos;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ProdutoIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProdutoRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar produto com sucesso e retornar estoqueBaixo")
    void deveCriarProdutoComSucesso() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.idProduto", notNullValue()))
                .andExpect(jsonPath("$.estoqueBaixo", is(true)));
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict quando nome do produto estiver duplicado")
    void deveRetornar409QuandoNomeDuplicado() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                3,
                10,
                "unidade"
        );

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.title", is("Produto já cadastrado")));
    }

    @Test
    @DisplayName("Deve retornar 422 Unprocessable Entity quando estoque atual for negativo")
    void deveRetornar422QuandoEstoqueNegativo() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Feijão 1kg",
                CategoriaProduto.ALIMENTO,
                -1,
                5,
                "unidade"
        );

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.detail", containsString("Estoque atual não pode ser negativo")));
    }

    @Test
    @DisplayName("Deve listar produtos com filtros de nome e categoria")
    void deveListarProdutosComFiltros() throws Exception {
        ProdutoRequestDTO arroz = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                15,
                10,
                "unidade"
        );
        ProdutoRequestDTO sabonete = new ProdutoRequestDTO(
                "Sabonete",
                CategoriaProduto.HIGIENE,
                30,
                10,
                "unidade"
        );

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arroz)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sabonete)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/produtos")
                        .param("nome", "Arroz")
                        .param("categoria", "ALIMENTO")
                        .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome", is("Arroz 5kg")));
    }
}
