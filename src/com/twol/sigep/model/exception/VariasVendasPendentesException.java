package com.twol.sigep.model.exception;

import java.util.List;

import com.twol.sigep.model.vendas.Venda;

public class VariasVendasPendentesException extends Exception {
	
	private List<Venda> vendasPendentes;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VariasVendasPendentesException(){
		super("Existe varias vendas nao finalizadas");
	}
	
	public VariasVendasPendentesException(String msg){
		super(msg);
	}

	public VariasVendasPendentesException(List<Venda> vendasPendentes){
		super("Existe varias vendas nao finalizadas");
		this.vendasPendentes = vendasPendentes;
	}
	
	public VariasVendasPendentesException(String msg , List<Venda> vendasPendentes){
		super(msg);
		this.vendasPendentes = vendasPendentes;
	}
	
	public List<Venda> getVendasPendentes() {
		return vendasPendentes;
	}

}
