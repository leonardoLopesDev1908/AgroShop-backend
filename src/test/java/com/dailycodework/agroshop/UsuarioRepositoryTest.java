package com.dailycodework.agroshop;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dailycodework.agroshop.model.Carrinho;
import com.dailycodework.agroshop.model.Endereco;
import com.dailycodework.agroshop.model.Role;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.EnderecoRepository;
import com.dailycodework.agroshop.repository.RoleRepository;
import com.dailycodework.agroshop.repository.UsuarioRepository;


@DataJpaTest
public class UsuarioRepositoryTest {
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Testando cadastro de usuário")
    void testCadastroUsuario(){
        Endereco e = new Endereco();
        e.setCep("94975152");
        e.setCidade("Porto Alegre");
        e.setComplemento("bloco h, num. 123");
        e.setEstado("RS");
        e.setNumero("321");
    
        Endereco enderecoSalvo = enderecoRepository.save(e);

        Role r = new Role();
        r.setNome("Funcionario");
        Role roleSalva = roleRepository.save(r);

        Carrinho c = new Carrinho();

        Usuario user = new Usuario();
        user.setCarrinho(c);
        user.setEmail("emaildementira@gmail.com");
        user.setEndereco(List.of(enderecoSalvo));
        user.setNome("Carlinhos");
        user.setRoles(List.of(roleSalva));
        user.setSobrenome("Almeida");
        user.setSenha(encoder.encode("12345678"));
        user.setTelefone("11998875544");    

        Usuario salvo = usuarioRepository.save(user);

        assertThat(salvo.getId()).isNotNull();
        
        boolean match = encoder.matches("12345678", salvo.getSenha());
        assertThat(match).isTrue();
        assertThat(salvo.getNome()).isEqualTo("Carlinhos");
        assertThat(salvo.getSobrenome()).isEqualTo("Almeida");
    }
}
