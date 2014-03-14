package com.twol.sigep.model.exception;

import com.twol.sigep.model.estoque.Promocao;

public class PromocaoValidaJaExistente extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PromocaoValidaJaExistente(Promocao p) {
		super("Promocao Valida Ja Existente para o produto: "+p.getProduto()+", id Promocao: "+p.getId());
	}

	
	
}
