package br.com.cadastro.models;

import java.io.Serializable;

public class CartaoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String numeroCartao;
	private String senha;
	private String valor;
	
	public String getNumeroCartao() {
		return numeroCartao;
	}
	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
