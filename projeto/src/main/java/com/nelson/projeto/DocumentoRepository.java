package com.nelson.projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelson.projeto.model.Documento;

// Use Long as the ID type (model uses Long)
public interface DocumentoRepository extends JpaRepository<Documento, Long> {}
