package br.com.cadastro.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;


@Data
@Entity
public class Cartao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6249733210874767507L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String numeroCartao;

	
	@Column(nullable = false)
	private String descricao;
	
	@Transient
	private String error;
	
	@Transient
	private Secrety secrety;

	@Transient
	private Credits credits;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}




	public String getNumeroCartao() {
		return numeroCartao;
	}


	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Secrety getSecrety() {
		return secrety;
	}


	public void setSecrety(Secrety secrety) {
		this.secrety = secrety;
	}


	public Credits getCredits() {
		return credits;
	}


	public void setCredits(Credits credits) {
		this.credits = credits;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
	
	
}
