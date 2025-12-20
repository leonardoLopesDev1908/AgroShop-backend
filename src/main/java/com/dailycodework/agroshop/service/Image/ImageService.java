package com.dailycodework.agroshop.service.Image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.agroshop.controller.dto.search.ImageSearchDTO;
import com.dailycodework.agroshop.controller.mapper.ImageMapper;
import com.dailycodework.agroshop.model.Image;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.repository.ImageRepository;
import com.dailycodework.agroshop.service.Product.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository repository;
    private final ProductService produtoService;
    private final ImageMapper mapper;

    @Override
    public Image buscarImagemPorId(Long id) {
        return repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Imagem não encontrada");
        });
    }

    @Override
    public void deletePorId(Long id) {
        repository.findById(id).ifPresentOrElse(repository::delete, () -> {
            throw new EntityNotFoundException("Imagem não encontrada");
        });
    }

    @Override
    public void updateImagem(MultipartFile file, Long id) {
        Image imagem = buscarImagemPorId(id);
        try {
            imagem.setArquivoNome(file.getOriginalFilename());
            imagem.setArquivoTipo(file.getContentType());
            imagem.setImage(new SerialBlob(file.getBytes()));
            repository.save(imagem);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ImageSearchDTO> salvarImagens(Long idProduto, List<MultipartFile> files) {
        Product produto = produtoService.buscarPorId(idProduto);
    
        List<ImageSearchDTO> imagens = new ArrayList<>();

        for(MultipartFile file : files){
            try {
                Image imagem = new Image();
                imagem.setArquivoNome(file.getOriginalFilename());
                imagem.setArquivoTipo(file.getContentType());
                imagem.setImage(new SerialBlob(file.getBytes()));
                imagem.setProduct(produto);

                String buildDownloadUrl = "/api/v1/images/imagem/download/";
                String downloadUrl = buildDownloadUrl + imagem.getId();
                imagem.setDownloadUrl(downloadUrl); 

                Image imagemSalva = repository.save(imagem);

                imagemSalva.setDownloadUrl(downloadUrl + imagemSalva.getId());
                repository.save(imagemSalva);

                ImageSearchDTO dto = mapper.toDTO(imagemSalva);

                imagens.add(dto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imagens;
    }
    
    
}
