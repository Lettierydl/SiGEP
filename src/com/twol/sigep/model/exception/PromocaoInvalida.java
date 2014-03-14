package com.twol.sigep.model.exception;

import com.twol.sigep.model.estoque.Promocao;

public class PromocaoInvalida extends Exception {

	public PromocaoInvalida(Promocao p) {
		super("Promocao Invalida, id: "+p.getId());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
