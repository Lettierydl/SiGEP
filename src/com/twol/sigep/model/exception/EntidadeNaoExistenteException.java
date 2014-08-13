package com.twol.sigep.model.exception;

public class EntidadeNaoExistenteException extends Exception {

	public EntidadeNaoExistenteException(String msg){
		super(msg);
	}
	
	public EntidadeNaoExistenteException(){
		super("Entidade não existente.");
	}
}
