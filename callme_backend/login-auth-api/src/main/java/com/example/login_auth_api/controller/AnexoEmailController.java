package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.AnexoEmail;
import com.example.login_auth_api.repositories.AnexoEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AnexoEmailRepository anexoEmailRepository;

    @GetMapping("/visualizar/{id}")
    public ResponseEntity<ByteArrayResource> visualizarAnexo(@PathVariable Long id) {
        AnexoEmail anexoEmail = anexoEmailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anexo não encontrado"));

        // Obtém o conteúdo binário do PDF
        byte[] conteudoPdf = anexoEmail.getConteudo();

        // Cria um ByteArrayResource para enviar o PDF
        ByteArrayResource resource = new ByteArrayResource(conteudoPdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + anexoEmail.getNomeArquivo())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(conteudoPdf.length)
                .body(resource);
    }
}
