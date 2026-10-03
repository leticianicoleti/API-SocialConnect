package br.com.socialconnect.api.produtos.repository;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdProdutoNot(String nome, Long idProduto);

    @Query("""
            select p from Produto p
            where (:nome is null or lower(p.nome) like lower(concat('%', :nome, '%')))
              and (:categoria is null or p.categoria = :categoria)
            """)
    Page<Produto> filtrar(
            @Param("nome") String nome,
            @Param("categoria") CategoriaProduto categoria,
            Pageable pageable
    );
}
