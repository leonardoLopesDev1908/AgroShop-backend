package com.dailycodework.agroshop.controller.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.agroshop.controller.dto.pesquisa.ImagemPesquisaDTO;
import com.dailycodework.agroshop.model.Imagem;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Imagem.IImagemService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/produto/imagens")
public class ImagemController {

    private final IImagemService service;
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImagens(@RequestParam("files") List<MultipartFile> files,
                                                    @RequestParam Long produtoId){
        try {
            List<ImagemPesquisaDTO> lista = service.salvarImagens(produtoId, files);
            return ResponseEntity.ok(new ApiResponse("Upload feito com sucesso!", lista));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Erro", e.getMessage()));
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadImagem(@PathVariable Long id) throws SQLException{
        Imagem imagem = service.buscarImagemPorId(id);
        
        ByteArrayResource resource = new ByteArrayResource(imagem.getImage()
            .getBytes(1, (int) imagem.getImage().length()));
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(imagem.getArquivoTipo()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ imagem.getArquivoNome()  + "\"")
            .body(resource);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateImagem(@RequestParam("file") MultipartFile file,
                                                @RequestParam Long produtoId){
        try {
            service.updateImagem(file, produtoId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("Sucesso!", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletarImagem(@PathVariable Long id){
        try {
            service.deletePorId(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Deletado", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Não encontrado", e.getMessage()));
        }
    }
}
