package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados retornados para um produto cadastrado")
public record ProdutoResponseDTO(
        @Schema(description = "Identificador único do produto", example = "1")
        Long idProduto,

        @Schema(description = "Nome único do produto", example = "Arroz 5kg")
        String nome,

        @Schema(description = "Categoria do produto", example = "ALIMENTO")
        CategoriaProduto categoria,

        @Schema(description = "Quantidade atual em estoque", example = "3")
        Integer estoqueAtual,

        @Schema(description = "Quantidade mínima desejada em estoque", example = "10")
        Integer estoqueMinimo,

        @Schema(description = "Unidade de medida", example = "unidade")
        String unidadeMedida,

        @Schema(description = "Data de cadastro", example = "2026-10-02")
        LocalDate dataCadastro,

        @Schema(description = "Indica se o estoque atual está abaixo do mínimo", example = "true")
        boolean estoqueBaixo
) {}
