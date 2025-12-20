package com.dailycodework.agroshop.repository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;   
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.model.Product;

@DataJpaTest 
public class ProdutoRepositoryTest {

    @Autowired
    private ProductRepository produtoRepository;

    @Autowired 
    private CategoryRepository categoriaRepository;
    
    @Test
    @DisplayName("Testando cadastro de produto")
    void testCadastroProduto(){
        Category c = new Category("Remedios");
        categoriaRepository.save(c);

        Product p = new Product();
        p.setNome("Antipulgas"); 
        p.setMarca("NexGard");
        p.setPreco(BigDecimal.valueOf(82.9));
        p.setEstoque(10);
        p.setCategory(c);
        p.setDescricao(
            "Antipulgas e Carrapatos NexGard para Cachorros de 4,1 a 10 Kg ( 28,3 mg ) � 1 Comprimido" 
        );
        p.setPeso(BigDecimal.valueOf(0.5));
        p.setAltura(BigDecimal.valueOf(20));
        p.setComprimento(BigDecimal.valueOf(10));
        p.setLargura(BigDecimal.valueOf(15));

        Product produtoSalvo = produtoRepository.save(p);

        assertThat(produtoSalvo.getId()).isNotNull();
        assertThat(produtoSalvo.getNome()).isEqualTo("Antipulgas");
        assertThat(produtoSalvo.getCategory()).isNotNull();
        assertThat(produtoSalvo.getCategory().getNome()).isEqualTo("Remedios");
        assertThat(produtoSalvo.getAltura()).isNotNull();
        assertThat(produtoSalvo.getPeso()).isNotNull();
        assertThat(produtoSalvo.getComprimento()).isNotNull();
        assertThat(produtoSalvo.getLargura()).isNotNull();

        assertThat(produtoRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testando update de produto")
    void testUpdateProduto(){
        Product p = new Product();
        p.setNome("Antipulgas"); 
        p.setMarca("NexGard");
        p.setPreco(BigDecimal.valueOf(82.9));
        p.setEstoque(10);
    
        Product salvo = produtoRepository.save(p);

        assertThat(salvo.getId()).isNotNull();

        Product resgatado = produtoRepository.findById(salvo.getId()).orElse(null);
        resgatado.setPreco(BigDecimal.valueOf(84.9));
        
        assertThat(resgatado).isNotNull();

        Product atualizado = produtoRepository.save(resgatado);

        assertThat(atualizado.getPreco()).isEqualTo(BigDecimal.valueOf(84.9));
        assertThat(atualizado).isNotNull();
    }

    @Test
    @DisplayName("Testando remoção de produto")
    void testDeleteProduto(){
        Category c = new Category("Remedios");
        categoriaRepository.save(c);

        Product p = new Product();
        p.setNome("Antipulgas"); 
        p.setMarca("NexGard");
        p.setPreco(BigDecimal.valueOf(82.9));
        p.setEstoque(10);
        p.setCategory(c);
        p.setDescricao(
            "Antipulgas e Carrapatos NexGard para Cachorros de 4,1 a 10 Kg ( 28,3 mg ) � 1 Comprimido" 
        );
        p.setPeso(BigDecimal.valueOf(0.5));
        p.setAltura(BigDecimal.valueOf(20));
        p.setComprimento(BigDecimal.valueOf(10));
        p.setLargura(BigDecimal.valueOf(15));

        Product produtoSalvo = produtoRepository.save(p);
        assertThat(produtoSalvo.getId()).isNotNull();
        
        produtoRepository.delete(produtoSalvo);

        boolean exists = produtoRepository.existsById(produtoSalvo.getId());
        assertThat(exists).isFalse();
    }

}