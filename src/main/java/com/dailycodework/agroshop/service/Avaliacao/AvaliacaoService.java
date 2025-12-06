package com.dailycodework.agroshop.service.Avaliacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.cadastro.AvaliacaoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.AvaliacaoPesquisaDTO;
import com.dailycodework.agroshop.controller.mapper.AvaliacaoMapper;
import com.dailycodework.agroshop.model.Avaliacao;
import com.dailycodework.agroshop.model.Produto;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.AvaliacaoRepository;
import com.dailycodework.agroshop.service.Produto.ProdutoService;
import com.dailycodework.agroshop.service.Usuario.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoService implements IAvaliacaoService{

    private final AvaliacaoRepository repository; 
    private final AvaliacaoMapper mapper;   
    private final UsuarioService userService;
    private final ProdutoService produtoService;
    
    @Override
    public List<AvaliacaoPesquisaDTO> findAvaliacoes(Long idProduto) {
        Produto produto = produtoService.buscarPorId(idProduto);
        return (repository.findAllByProduto(produto)).stream()  
                        .map(mapper::toDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public AvaliacaoPesquisaDTO addAvaliacao(AvaliacaoCadastroDTO dto, Long idProduto) {
        Avaliacao avaliacao = mapper.toEntity(dto);
        avaliacao.setCodigoPublico(UUID.randomUUID().toString());
        
        Produto produto = produtoService.buscarPorId(idProduto);
        produto.getAvaliacoes().add(avaliacao);
        
        Usuario user = userService.getAuthenticatedUsuario();
        user.getAvaliacoes().add(avaliacao);

        avaliacao.setData(LocalDateTime.now());
        avaliacao.setProduto(produto);
        avaliacao.setUsuario(user);

        return mapper.toDTO(repository.save(avaliacao));
    }

    @Override
    public void deleteAvaliacao(Usuario user, Long id) throws AuthorizationException {
        Avaliacao avaliacao = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Avaliação não encontrada para esse id: " + id);
        });

        if(avaliacao.getUsuario().getId().equals(user.getId())){
            repository.delete(avaliacao);
        } else {
            throw new AuthorizationException("Você não tem permissão para realizar essa ação");
        }
    }

    @Override
    public boolean verificarAvaliacao(Usuario user, Long idProduto){
        Produto produto = produtoService.buscarPorId(idProduto);
        boolean existe = repository.existsByUsuarioAndProduto(user, produto);
        return existe;
    }
    
}
