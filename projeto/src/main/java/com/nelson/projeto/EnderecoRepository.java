package com.nelson.projeto;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelson.projeto.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {}
