package com.nelson.projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelson.projeto.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {}
