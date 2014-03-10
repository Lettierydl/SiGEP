package com.twol.sigep.model.pessoas;

import java.util.Collections;
import java.util.List;

import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.vendas.FinderVenda;
import com.twol.sigep.model.vendas.Venda;

public class ControllerPagamento {

	
	//retorna o troco e 0 caso não tenha troco
	//caso o pagamento vinher com o valor maior do que o cliente pode receber(vendas) é setado o valor
	public double cadastrarPagamento(Pagamento newPagamento) throws ParametrosInvalidosException{
		Cliente c = newPagamento.getCliente();
		List<Venda> vendas = FinderVenda.vendasNaoPagaDoCliente(c);
		Collections.sort(vendas);
		double valorRestante = newPagamento.getValor();
		for (Venda vend : vendas) {
			double parteNaoPagaDaVenda = vend.getValorNaoPagoDaVenda();
			if(parteNaoPagaDaVenda > valorRestante){
				vend.acrescentarPartePagaDaVenda(valorRestante);
				valorRestante = 0;
				Venda.atualizar(vend);
				break;
			}else{
				valorRestante -= parteNaoPagaDaVenda;
				vend.acrescentarPartePagaDaVenda(parteNaoPagaDaVenda);
				Venda.atualizar(vend);
			}
		}
		newPagamento.setValor(newPagamento.getValor()-valorRestante);
		c.diminuirDebito(newPagamento.getValor()-valorRestante);
		Cliente.atualizar(c);
		Pagamento.salvar(newPagamento);
		return valorRestante;
	}
	
	
	
}
