package br.com.cadastro.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cadastro.entity.Secrety;

@Repository
public interface SecretyRepository extends JpaRepository<Secrety, Long>{

	List<Secrety> findBySequenceCartao(String sequenceCartao);


}
