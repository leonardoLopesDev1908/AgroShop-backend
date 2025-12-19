package com.dailycodework.agroshop.service.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.pesquisa.ProductSearchDTO;
import com.dailycodework.agroshop.controller.dto.register.ProductRegisterDTO;
import com.dailycodework.agroshop.controller.dto.update.ProductUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.ProductMapper;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.model.CartItem;
import com.dailycodework.agroshop.model.OrderItem;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.repository.CategoriaRepository;
import com.dailycodework.agroshop.repository.ItemCarrinhoRepository;
import com.dailycodework.agroshop.repository.ItemPedidoRepository;
import com.dailycodework.agroshop.repository.ProdutoRepository;
import com.dailycodework.agroshop.service.Category.CategoryService;

import static com.dailycodework.agroshop.repository.specs.ProdutosSpecs.categoriaEqual;
import static com.dailycodework.agroshop.repository.specs.ProdutosSpecs.precoBetween;
import static com.dailycodework.agroshop.repository.specs.ProdutosSpecs.searchLike;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProdutoRepository repository;
    private final ProductValidator validator;
    private final ProductMapper mapper;

    private final CategoryService categoriaService;
    private final CategoriaRepository categoriaRepository;

    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    public Product addProduto(ProductRegisterDTO dto) {
        Product produto = mapper.toEntity(dto);
        validator.validarCriacaoProduto(produto);

        Category categoria = Optional.ofNullable(categoriaRepository.findByNome(produto.getCategoria().getNome()))
            .orElseGet(() -> {
                Category novaCategoria = new Category(produto.getCategoria().getNome());
                return categoriaRepository.save(novaCategoria);
            });

        produto.setCategoria(categoria);

        return repository.save(produto);
    }

    @Override
    public Product buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Nenhum produto encontrado com esse ID");
        });
    }

    @Override
    @Transactional
    public Product atualizarProduto(Long id, ProductUpdateDTO dtoNovoProduto) {
        Product produtoExistente = repository.findById(id).orElseThrow(() ->{
            throw new EntityNotFoundException("Nenhum produto com esse ID");
        });

        mapper.updateProdutoFromDto(dtoNovoProduto, produtoExistente);

        validator.validarAtualizacaoProduto(produtoExistente, id);

        return repository.save(produtoExistente);
    }

    @Override
    public void deletarProdutoPorId(Long id) {
        repository.findById(id)
            .ifPresentOrElse((var produto) -> {
                List<CartItem> itens = itemCarrinhoRepository.findByProdutoId(id);
                itens.forEach(item -> {
                    Cart carrinho = item.getCarrinho();
                    carrinho.removeItem(item);
                    itemCarrinhoRepository.delete(item);
                });

                List<OrderItem> itensPedido =  itemPedidoRepository.findByProdutoId(id);
                itensPedido.forEach(item -> {
                    item.setProduto(null);
                    itemPedidoRepository.save(item);
                });

                Optional.ofNullable(produto.getCategoria())
                        .ifPresent(categoria -> categoria.getProdutos().remove(produto));
                produto.setCategoria(null);

                repository.deleteById(produto.getId());
            }, () -> {
                throw new EntityNotFoundException("Nenhum produto encontrado com esse ID");
            });
    }

    @Override
    public Page<Product> getProdutos(String search, String categoria, BigDecimal precoMin, 
                                        BigDecimal precoMax, Integer pagina, Integer tamanhoPagina) {

        Specification<Product> specs = null;

        Category categoriaSearched = categoriaService.buscaPorNome(categoria);

        if(search != null && !search.isEmpty()){
            specs = (specs == null) ? searchLike(search) : specs.and(searchLike(search));
        }
        if(precoMin != null || precoMax != null){
            specs = (specs == null) ? precoBetween(precoMin, precoMax) : specs.and(precoBetween(precoMin, precoMax));
        }
        if(categoriaSearched != null){
            specs = (specs == null) ? categoriaEqual(categoriaSearched) : specs.and(categoriaEqual(categoriaSearched));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return repository.findAll(specs, pageRequest);
    }

    @Override
    public List<Product> getAllProdutos(){
        return repository.findAll();
    }

    @Override
    public List<Product> getProdutoPorMarcaECategoria(String categoria, String marca) {
        throw new RuntimeException("");
    }

    @Override
    public List<Product> getProdutoPorMarcarENome(String marca, String nome) {
        return repository.findByMarcaAndNome(marca, nome);
    }

    @Override
    public List<ProductSearchDTO> getProdutoPorNome(String nome) {
       return repository.findByNomeContaining(nome).stream()
                                    .map(mapper::toDTO)
                                    .collect(Collectors.toList());
    }

    @Override
    public List<ProductSearchDTO> getProdutoPorMarca(String marca) {
        return repository.findByMarcaContaining(marca).stream()
                                .map(mapper::toDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSearchDTO> getProdutoPorCategoria(String categoriaStr) {
        Category categoria = categoriaService.buscaPorNome(categoriaStr);
        return repository.findByCategoria(categoria).stream()
                                        .map(mapper::toDTO)
                                        .collect(Collectors.toList());
    }

    @Override
    public List<ProductSearchDTO> findOutrosProdutos(String categoriaStr){
        Category categoria = categoriaService.buscaPorNome(categoriaStr);
        return repository.findTop10ByCategoriaNotOrCategoriaIsNull(categoria).stream()
                                    .map(mapper::toDTO)
                                    .collect(Collectors.toList());
    }

    @Override
    public List<Product> findDistinctProdutodsByNome(){
        List<Product> produtos = getAllProdutos();
        Map<String, Product> distintos = produtos.stream()  
            .collect(Collectors.toMap(Product::getNome, 
                                      produto -> produto, 
                                      (existing, replacement) -> existing));
        return new ArrayList<>(distintos.values());
    }
    
    @Override
    public Integer getEstoque(Long id){
        return repository.findEstoqueById(id);
    }
}
