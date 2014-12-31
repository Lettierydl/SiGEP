package com.twol.sigep.model.configuracoes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.twol.sigep.model.vendas.Venda;

public class ConfiguracaoDeVenda {
	
	private static ConfiguracaoDeVenda ins = null;
		
	private ConfiguracaoDeVenda(){}
	private Map<HttpSession, Venda> ventasSendoUsadas = new HashMap<HttpSession, Venda>();
	
	public static ConfiguracaoDeVenda getInstance(){
		if(ins == null){
			ins = new ConfiguracaoDeVenda();
		}
		return ins;
	}
	
	public void addRegistroUsoDeVenda(Venda v, HttpSession session){
		ventasSendoUsadas.put(session, v);
	}
	
	public Venda getRegistroUsoDeVenda(HttpSession session){
		return ventasSendoUsadas.get(session);
	}
	
	public Collection<Venda> getVentasUsadas(){
		return ventasSendoUsadas.values();
	}

	public boolean contensId(int id) {
		for(Venda v : ventasSendoUsadas.values()){
			if(v.getId() == id){
				return true;
			}
		}
		return false;
	}
	
}
