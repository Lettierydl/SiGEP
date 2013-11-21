package com.twol.sigep.model.exception;

public class LinhaDaVendaNaoEncontradaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LinhaDaVendaNaoEncontradaException(){
		super("A Linha da Venda não foi encontrada");
	}

}
