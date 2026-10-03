package br.com.socialconnect.api.exception;

public class EstoqueNegativoException extends RuntimeException {

    public EstoqueNegativoException() {
        super("Estoque atual não pode ser negativo.");
    }
}
