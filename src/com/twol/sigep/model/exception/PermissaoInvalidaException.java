package com.twol.sigep.model.exception;

public class PermissaoInvalidaException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissaoInvalidaException(){
		super("Você não possue permissão para realizar esta operação.");
	}
	
}
