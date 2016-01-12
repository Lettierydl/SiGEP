package com.twol.sigep.model.vendas;

import java.util.Calendar;

import com.twol.sigep.model.pessoas.Cliente;

public interface Pagavel {

	
	public double getTotal();
	
	public double getPartePaga();
	
	public double getValorNaoPago();
	
	public boolean isPaga();
	
	public Cliente getCliente();
	
	public Calendar getDia();
	
	public String getDescricao();

	public void acrescentarPartePaga(double partePaga);
	
	
}
