package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.ProdutoNomeDuplicadoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

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
        Produto produtoSalvo = Produto.builder()
                .idProduto(1L)
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        Mockito.when(repository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(Produto.class))).thenReturn(produtoSalvo);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = service.criar(dto);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertNotNull(resultado.idProduto());
        Assertions.assertTrue(resultado.estoqueBaixo());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando estoque negativo")
    void deveLancarExcecaoQuandoEstoqueNegativo() {
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
        // ACT + ASSERT: Executar e verificar exceção
        // ==========================================
        Assertions.assertThrows(EstoqueNegativoException.class, () -> service.criar(dto));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome duplicado")
    void deveLancarExcecaoQuandoNomeDuplicado() {
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
        Mockito.when(repository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(true);

        // ==========================================
        // ACT + ASSERT: Executar e verificar exceção
        // ==========================================
        Assertions.assertThrows(ProdutoNomeDuplicadoException.class, () -> service.criar(dto));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Produto.class));
    }
}
