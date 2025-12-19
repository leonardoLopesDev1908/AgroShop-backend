package com.dailycodework.agroshop.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;   //
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.Role;
import com.dailycodework.agroshop.model.User;


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

    @TestConfiguration
    static class Config {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        }
    }

    @Test
    @DisplayName("Testando cadastro de usuário")
    void testCadastroUsuario(){
        Address e = new Address();
        e.setCep("94975152");
        e.setCidade("Porto Alegre");
        e.setComplemento("bloco h, num. 123");
        e.setEstado("RS");
        e.setNumero("321");
    
        Address enderecoSalvo = enderecoRepository.save(e);

        Role r = new Role();
        r.setNome("Funcionario");
        Role roleSalva = roleRepository.save(r);

        Cart c = new Cart();

        User user = new User();
        user.setCarrinho(c);
        user.setEmail("emaildementira@gmail.com");
        user.setEndereco(List.of(enderecoSalvo));
        user.setNome("Carlinhos");
        user.setRoles(List.of(roleSalva));
        user.setSobrenome("Almeida");
        user.setSenha(encoder.encode("12345678"));
        user.setTelefone("11998875544");    

        User salvo = usuarioRepository.save(user);

        assertThat(salvo.getId()).isNotNull();
        
        boolean match = encoder.matches("12345678", salvo.getSenha());
        assertThat(match).isTrue();
        assertThat(salvo.getNome()).isEqualTo("Carlinhos");
        assertThat(salvo.getSobrenome()).isEqualTo("Almeida");
    }
}
