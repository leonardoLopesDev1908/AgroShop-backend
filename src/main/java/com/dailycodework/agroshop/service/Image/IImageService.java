package com.dailycodework.agroshop.service.Image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.agroshop.controller.dto.pesquisa.ImageSearchDTO;
import com.dailycodework.agroshop.model.Image;

public interface IImageService {
    
    Image buscarImagemPorId(Long id);
    void deletePorId(Long id);
    void updateImagem(MultipartFile file, Long id);
    List<ImageSearchDTO> salvarImagens(Long idProduto, List<MultipartFile> files);

}
