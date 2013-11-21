package com.twol.sigep.model.exception;

public class ParametrosInvalidosException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParametrosInvalidosException(){
		super("Valor de compo inválido para esta operação");
	}
}
