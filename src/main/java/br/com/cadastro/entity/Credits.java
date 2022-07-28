package br.com.cadastro.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

//@JsonIgnore
@Data
@Entity
public class Credits implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private BigDecimal valor;
	
	@Column(nullable = false)
	private String sequenceCartao;
	
	@Column(nullable = false)
	private String tipoTransacao;

	@Column(nullable = false)
	private BigDecimal saldoCartao;
	
	@Column(nullable = false)
	private String dataTransacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getSequenceCartao() {
		return sequenceCartao;
	}

	public void setSequenceCartao(String sequenceCartao) {
		this.sequenceCartao = sequenceCartao;
	}

	public String getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public BigDecimal getSaldoCartao() {
		return saldoCartao;
	}

	public void setSaldoCartao(BigDecimal saldoCartao) {
		this.saldoCartao = saldoCartao;
	}

	public String getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(String dataTransacao) {
		this.dataTransacao = dataTransacao;
	}





	
	
	
}
