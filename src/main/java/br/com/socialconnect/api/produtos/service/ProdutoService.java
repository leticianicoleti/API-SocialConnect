package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.ProdutoNomeDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        String nomeFiltro = nome != null && !nome.isBlank() ? nome : null;
        return repository.filtrar(nomeFiltro, categoria, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long idProduto) {
        return repository.findById(idProduto)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + idProduto));
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarEstoqueAtual(dto.estoqueAtual());
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new ProdutoNomeDuplicadoException(dto.nome());
        }

        Produto entity = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        return toResponseDTO(repository.save(entity));
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long idProduto, ProdutoRequestDTO dto) {
        validarEstoqueAtual(dto.estoqueAtual());
        Produto entity = repository.findById(idProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado com o ID: " + idProduto));

        if (repository.existsByNomeIgnoreCaseAndIdProdutoNot(dto.nome(), idProduto)) {
            throw new ProdutoNomeDuplicadoException(dto.nome());
        }

        entity.setNome(dto.nome());
        entity.setCategoria(dto.categoria());
        entity.setEstoqueAtual(dto.estoqueAtual());
        entity.setEstoqueMinimo(dto.estoqueMinimo());
        entity.setUnidadeMedida(dto.unidadeMedida());

        return toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void deletar(Long idProduto) {
        if (!repository.existsById(idProduto)) {
            throw new RecursoNaoEncontradoException(
                    "Produto não encontrado com o ID: " + idProduto);
        }
        repository.deleteById(idProduto);
    }

    private void validarEstoqueAtual(Integer estoqueAtual) {
        if (estoqueAtual != null && estoqueAtual < 0) {
            throw new EstoqueNegativoException();
        }
    }

    private ProdutoResponseDTO toResponseDTO(Produto entity) {
        boolean estoqueBaixo = entity.getEstoqueAtual() < entity.getEstoqueMinimo();
        return new ProdutoResponseDTO(
                entity.getIdProduto(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getEstoqueAtual(),
                entity.getEstoqueMinimo(),
                entity.getUnidadeMedida(),
                entity.getDataCadastro(),
                estoqueBaixo
        );
    }
}
