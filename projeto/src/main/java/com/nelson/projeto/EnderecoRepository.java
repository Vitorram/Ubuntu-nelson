package com.nelson.projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelson.projeto.model.Endereco;

// Use Long as the ID type (model uses Long)
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {}
