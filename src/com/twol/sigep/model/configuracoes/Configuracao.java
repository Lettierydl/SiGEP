package com.twol.sigep.model.configuracoes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.twol.sigep.model.pessoas.TipoDeFuncionario;

@Table(name = "configuracao")
@Entity
@IdClass(value = ConfiguracaoPK.class)
public class Configuracao {

	@Id
	private String chave;// nome

	@Id
	private TipoDeFuncionario tipoDeFuncionario;
	
	@Column
	private boolean valor;

	public Configuracao(String chave, boolean valor, TipoDeFuncionario tipo) {
		this.chave = chave;
		this.valor = valor;
		this.tipoDeFuncionario = tipo;
	}

	public Configuracao(){}
	
	
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public boolean isValor() {
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public TipoDeFuncionario getTipoDeFuncionario() {
		return tipoDeFuncionario;
	}

	public void setTipoDeFuncionario(TipoDeFuncionario tipoDeFuncionario) {
		this.tipoDeFuncionario = tipoDeFuncionario;
	}

}

