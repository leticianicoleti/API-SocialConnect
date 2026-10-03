package br.com.socialconnect.api.produtos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class UnidadeMedidaValidator implements ConstraintValidator<UnidadeMedidaValida, String> {

    private static final Set<String> UNIDADES_PERMITIDAS = Set.of(
            "kg",
            "g",
            "litro",
            "ml",
            "unidade",
            "pacote",
            "caixa"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return UNIDADES_PERMITIDAS.contains(value.trim().toLowerCase());
    }
}
