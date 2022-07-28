package br.com.cadastro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cadastro.entity.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long>{

	Cartao findByNumeroCartao(String numeroCartao);


}
