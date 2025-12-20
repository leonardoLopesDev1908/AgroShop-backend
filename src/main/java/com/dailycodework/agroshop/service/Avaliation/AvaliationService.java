package com.dailycodework.agroshop.service.Avaliation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.register.AvaliationRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AvaliationSearchDTO;
import com.dailycodework.agroshop.controller.mapper.AvaliationMapper;
import com.dailycodework.agroshop.model.Avaliacao;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.AvaliacaoRepository;
import com.dailycodework.agroshop.service.Product.ProductService;
import com.dailycodework.agroshop.service.User.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliationService implements IAvaliationService{

    private final AvaliacaoRepository repository; 
    private final AvaliationMapper mapper;   
    private final UserService userService;
    private final ProductService produtoService;
    
    @Override
    public List<AvaliationSearchDTO> findAvaliacoes(Long idProduto) {
        Product produto = produtoService.buscarPorId(idProduto);
        return (repository.findAllByProduct(produto)).stream()  
                        .map(mapper::toDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public AvaliationSearchDTO addAvaliacao(AvaliationRegisterDTO dto, Long idProduto) {
        Avaliacao avaliacao = mapper.toEntity(dto);
        avaliacao.setCodigoPublico(UUID.randomUUID().toString());
        
        Product produto = produtoService.buscarPorId(idProduto);
        produto.getAvaliacoes().add(avaliacao);
        
        User user = userService.getAuthenticatedUsuario();
        user.getAvaliacoes().add(avaliacao);

        avaliacao.setData(LocalDateTime.now());
        avaliacao.setProduct(produto);
        avaliacao.setUser(user);

        return mapper.toDTO(repository.save(avaliacao));
    }

    @Override
    public void deleteAvaliacao(User user, Long id) throws AuthorizationException {
        Avaliacao avaliacao = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Avaliação não encontrada para esse id: " + id);
        });

        if(avaliacao.getUser().getId().equals(user.getId())){
            repository.delete(avaliacao);
        } else {
            throw new AuthorizationException("Você não tem permissão para realizar essa ação");
        }
    }

    @Override
    public boolean verificarAvaliacao(User user, Long idProduto){
        Product produto = produtoService.buscarPorId(idProduto);
        boolean existe = repository.existsByUserAndProduct(user, produto);
        return existe;
    }
    
}
