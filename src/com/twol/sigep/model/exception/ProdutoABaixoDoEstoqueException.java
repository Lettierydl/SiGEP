package com.twol.sigep.model.exception;

public class ProdutoABaixoDoEstoqueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProdutoABaixoDoEstoqueException(){
		super("Produto com quantidade de estoque negativa");
	}

}
