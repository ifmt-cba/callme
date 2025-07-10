package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.AnexoEmail;
import com.example.login_auth_api.repositories.AnexoEmailRepository;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anexos")
public class AnexoEmailController {

    private final AnexoEmailRepository anexoEmailRepository;
    private final LogPort log;

    public AnexoEmailController(AnexoEmailRepository anexoEmailRepository, LogPort log) {
        this.anexoEmailRepository = anexoEmailRepository;
        this.log = log;
    }

    @GetMapping("/visualizar/{id}")
    public ResponseEntity<ByteArrayResource> visualizarAnexo(@PathVariable Long id) {
        log.trace(String.format("Iniciando visualização de anexo | ID: %d", id));

        AnexoEmail anexoEmail = anexoEmailRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("Anexo não encontrado | ID: %d", id));
                    return new RuntimeException("Anexo não encontrado");
                });

        byte[] conteudoPdf = anexoEmail.getConteudo();
        log.info(String.format("Anexo encontrado com sucesso | Nome: %s | Tamanho: %d bytes | ID: %d",
        anexoEmail.getNomeArquivo(), conteudoPdf.length, id));

        if (conteudoPdf.length == 0) {
            log.error(String.format("Anexo encontrado está vazio | Nome: %s | ID: %d", anexoEmail.getNomeArquivo(), id));
        }


        ByteArrayResource resource = new ByteArrayResource(conteudoPdf);
        log.debug(String.format("Criado ByteArrayResource | ID: %d", id));
        
        
        log.trace(String.format("Finalizando visualização de anexo | ID: %d", id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + anexoEmail.getNomeArquivo())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(conteudoPdf.length)
                .body(resource);
    }
}
