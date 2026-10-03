package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.validation.UnidadeMedidaValida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de produto")
public record ProdutoRequestDTO(
        @Schema(description = "Nome único do produto", example = "Arroz 5kg")
        @NotBlank(message = "{produto.nome.obrigatorio}")
        @Size(max = 150, message = "{produto.nome.tamanho}")
        String nome,

        @Schema(description = "Categoria do produto: ALIMENTO, ROUPA, HIGIENE ou OUTROS", example = "ALIMENTO")
        @NotNull(message = "{produto.categoria.obrigatoria}")
        CategoriaProduto categoria,

        @Schema(description = "Quantidade atual em estoque", example = "3")
        @NotNull(message = "{produto.estoqueAtual.obrigatorio}")
        Integer estoqueAtual,

        @Schema(description = "Quantidade mínima desejada em estoque", example = "10")
        @NotNull(message = "{produto.estoqueMinimo.obrigatorio}")
        @Min(value = 0, message = "{produto.estoqueMinimo.naoNegativo}")
        Integer estoqueMinimo,

        @Schema(description = "Unidade de medida do produto", example = "unidade")
        @NotBlank(message = "{produto.unidadeMedida.obrigatoria}")
        @Size(max = 20, message = "{produto.unidadeMedida.tamanho}")
        @UnidadeMedidaValida
        String unidadeMedida
) {}
