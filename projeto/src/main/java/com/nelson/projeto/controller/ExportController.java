package com.nelson.projeto.controller;

import com.nelson.projeto.EnderecoRepository;
import com.nelson.projeto.DocumentoRepository;
import com.nelson.projeto.UserRepository;
import com.nelson.projeto.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/export")
@CrossOrigin(origins = "*")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Exporta endereços em CSV
     */
    @GetMapping("/enderecos/csv")
    public ResponseEntity<byte[]> exportarEnderecosCSV() {
        try {
            byte[] csvBytes = exportService.exportarEnderecosCSV(
                    (java.util.List) enderecoRepository.findAll()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=enderecos.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Exporta documentos em JSON
     */
    @GetMapping("/documentos/json")
    public ResponseEntity<String> exportarDocumentosJSON() {
        try {
            String jsonData = exportService.exportarDocumentosJSON(
                    (java.util.List) documentoRepository.findAll()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documentos.json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonData);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Exporta nomes de usuários em PDF
     */
    @GetMapping("/usuarios/pdf")
    public ResponseEntity<byte[]> exportarUsuariosNomesPDF() {
        try {
            byte[] pdfBytes = exportService.exportarUsuariosNomesPDF(
                    (java.util.List) userRepository.findAll()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Exporta todos os dados em JSON (usuários, endereços, documentos)
     */
    @GetMapping("/todos/json")
    public ResponseEntity<String> exportarTodosDadosJSON() {
        try {
            String jsonData = exportService.exportarTodosDadosJSON(
                    (java.util.List) userRepository.findAll()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=todos_dados.json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonData);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
