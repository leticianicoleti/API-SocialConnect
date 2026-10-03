package br.com.socialconnect.api.exception;

public class ProdutoNomeDuplicadoException extends RuntimeException {

    public ProdutoNomeDuplicadoException(String nome) {
        super("Produto já cadastrado com o nome: " + nome);
    }
}
