package com.nelson.projeto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.nelson.projeto.model.Documento;
import com.nelson.projeto.model.Endereco;
import com.nelson.projeto.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

@Service
public class ExportService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Exporta endereços em formato CSV
     */
    public byte[] exportarEnderecosCSV(List<Endereco> enderecos) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(output);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Rua", "Número", "Cidade", "Estado", "Usuário ID"))) {

            for (Endereco endereco : enderecos) {
                csvPrinter.printRecord(
                        endereco.getId(),
                        endereco.getRua(),
                        endereco.getNumero(),
                        endereco.getCidade(),
                        endereco.getEstado(),
                        endereco.getUser() != null ? endereco.getUser().getId() : ""
                );
            }
            csvPrinter.flush();
        }
        return output.toByteArray();
    }

    /**
     * Exporta documentos em formato JSON
     */
    public String exportarDocumentosJSON(List<Documento> documentos) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(documentos);
    }

    /**
     * Exporta nomes de usuários em formato PDF
     */
    public byte[] exportarUsuariosNomesPDF(List<User> usuarios) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(output);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("LISTA DE USUÁRIOS").setBold().setFontSize(18));
        document.add(new Paragraph(" "));

        int contador = 1;
        for (User usuario : usuarios) {
            document.add(new Paragraph(contador + ". " + usuario.getName())
                    .setFontSize(12));
            contador++;
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total de usuários: " + usuarios.size())
                .setBold());

        document.close();
        return output.toByteArray();
    }

    /**
     * Exporta todos os dados (usuários, endereços e documentos)
     */
    public String exportarTodosDadosJSON(List<User> usuarios) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(usuarios);
    }
}
