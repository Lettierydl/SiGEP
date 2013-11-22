package com.twol.sigep.model.vendas;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

public class FinderVenda {

	@SuppressWarnings("unchecked")
	public static List<Venda> vendasDoFuncionarioEntreDias(Funcionario f, Cliente c, Dependente d, Calendar dataInicio,
			Calendar dataFim, FormaDePagamento fpag) {
		if (f == null) {
			throw new IllegalArgumentException(
					"É necessário uma Funcionario válida");
		}
		String consulta = "select v from Venda v where v.funcionario = :func";
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("func", f);
		if (dataInicio != null && dataFim != null) {
			parametros.put("dataInicio", dataInicio);
			parametros.put("dataFim", dataFim);
			consulta += " and v.dia between :dataInicio and :dataFim";
		} else if (dataInicio != null) {
			parametros.put("dataInicio", dataInicio);
			consulta += " and v.dia =:dataInicio";
		} else if (dataFim != null) {
			parametros.put("dataFim", dataFim);
			consulta += " and v.dia =:dataFim";
		}
		if (c != null) {
			parametros.put("cli", c);
			consulta += " and v.cliente :cli";
		}
		if (d != null) {
			parametros.put("dep", d);
			consulta += " and v.dependente :dep";
		}
		if (fpag != null) {
			parametros.put("fpag", d);
			consulta += " and v.formaDePagamento :fpag";
		}
		Query query = Persistencia.em.createQuery(consulta, Venda.class);
		for (Map.Entry<String, Object> param : parametros.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		List<Venda> Vendas = (List<Venda>) query.getResultList();
		return Vendas;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Venda> vendasDoFuncionario(Funcionario f) {
		if (f == null)
			throw new IllegalArgumentException(
					"É necessário uma Funcionario válida");
		Query query = Persistencia.em.createQuery(
				"select v from Venda as v where v.funcionario = :func"
				,Venda.class);
		query.setParameter("func", f);
		List<Venda> Vendas = (List<Venda>) query.getResultList();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> vendasEntreAsDatas(
			Calendar diaInicio, Calendar diaFim) {
		Query query = Persistencia.em.createQuery(
				"select v from Venda as v where v.dia between :diaInicio and :diaFim"
				,Venda.class);
		query.setParameter("diaInicio", diaInicio);
		query.setParameter("diaFim", diaFim);
		List<Venda> Vendas = (List<Venda>) query.getResultList();
		return Vendas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> getVendasNaoFinalizadas(){
		Query query = Persistencia.em.createQuery("select v from Venda as v " +
				"where v.formaDePagamento is NULL", Venda.class);
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Venda> getVendasNaoFinalizadasPorFuncionario(Funcionario f){
		Query query = Persistencia.em.createQuery("select v from Venda as v " +
				"where v.formaDePagamento is NULL and v.funcionario = :func", Venda.class);
		query.setParameter("func", f);
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

}
