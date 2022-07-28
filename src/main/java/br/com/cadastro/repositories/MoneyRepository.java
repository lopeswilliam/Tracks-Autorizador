package br.com.cadastro.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cadastro.entity.Credits;

@Repository
public interface MoneyRepository extends JpaRepository<Credits, Long>{

	List<Credits> findBySequenceCartao(String id);
	
	
}
