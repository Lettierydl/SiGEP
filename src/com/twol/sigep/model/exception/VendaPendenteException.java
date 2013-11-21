package com.twol.sigep.model.exception;

public class VendaPendenteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VendaPendenteException(){
		super("Ainda existe uma venda não finalizada");
	}

}
