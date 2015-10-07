package com.twol.sigep.controller.find;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.FormaDePagamento;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.Persistencia;

public class FindVenda {

	private static final int LIMITE_DA_LISTA_DE_VENDAS_DE_HOJE = 20;

	@SuppressWarnings("unchecked")
	public static List<Venda> vendasDoFuncionarioEntreDias(Funcionario f,
			Cliente c, Dependente d, Calendar dataInicio, Calendar dataFim,
			FormaDePagamento fpag) {
		if (f == null) {
			throw new IllegalArgumentException(
					"�� necess��rio uma Funcionario v��lida");
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
					"�� necess��rio uma Funcionario v��lida");
		Query query = Persistencia.em.createQuery(
				"select v from Venda as v where v.funcionario = :func",
				Venda.class);
		query.setParameter("func", f);
		List<Venda> Vendas = (List<Venda>) query.getResultList();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> vendasEntreAsDatas(Calendar diaInicio,
			Calendar diaFim) {
		Query query = Persistencia.em
				.createQuery(
						"select v from Venda as v where v.dia between :diaInicio and :diaFim",
						Venda.class);
		query.setParameter("diaInicio", diaInicio);
		query.setParameter("diaFim", diaFim);
		List<Venda> Vendas = (List<Venda>) query.getResultList();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public List<Venda> getVendasNaoFinalizadas() {
		Query query = Persistencia.em.createQuery("select v from Venda as v "
				+ "where v.formaDePagamento is NULL", Venda.class);
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

	@SuppressWarnings("unchecked")
	public List<Venda> getVendasNaoFinalizadasPorFuncionario(Funcionario f) {
		Query query = Persistencia.em.createQuery("select v from Venda as v "
				+ "where v.formaDePagamento is NULL and v.funcionario = :func",
				Venda.class);
		query.setParameter("func", f);
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

	public static List<Venda> vendasNaoPagaDeHoje() {
		String stringQuery = "select v FROM Venda as v ";
		stringQuery += "WHERE v.paga = false and day(v.dia) = day(curdate()) and v.dia >= curdate() "
				+ "and v.cliente != null order by v.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		query.setMaxResults(LIMITE_DA_LISTA_DE_VENDAS_DE_HOJE);

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

	public static List<Venda> vendasDeHoje() {
		String stringQuery = "select v FROM Venda as v ";
		stringQuery += "WHERE day(v.dia) = day(curdate()) and v.dia >= curdate()"
				+ " order by v.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		query.setMaxResults(LIMITE_DA_LISTA_DE_VENDAS_DE_HOJE);

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}
	
	public static List<Venda> vendasDoCliente(Cliente cliente, Date diaInicio,
			Date diaFim) {
		
		Calendar di = Calendar.getInstance();
		di.setTime(diaInicio);
		di.set(Calendar.HOUR, 0);
		di.set(Calendar.MINUTE, 0);
		di.set(Calendar.SECOND, 00);
		
		Calendar df = Calendar.getInstance();
		df.setTime(diaFim);
		df.set(Calendar.HOUR, 23);
		df.set(Calendar.MINUTE, 59);
		df.set(Calendar.SECOND, 59);
		
		String stringQuery = "select v FROM Venda as v ";
		stringQuery += "WHERE v.cliente = :cli and  v.dia between :diaInicio and :diaFim"
				+ " order by v.dia , v.total DESC";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		query.setParameter("cli", cliente);
		query.setParameter("diaInicio", di);
		query.setParameter("diaFim", df);

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

	public static List<Venda> vendasNaoPagasDosClientes(List<Cliente> clientes) {
		String stringQuery = "select v FROM Venda as v ";
		stringQuery += "WHERE v.paga = false ";

		for (int i = 0; i < clientes.size(); i++) {
			if (i > 0) {
				stringQuery += "or v.cliente = :cli" + i + " ";
			} else {
				stringQuery += "and ( v.cliente = :cli" + i + " ";
			}
		}

		stringQuery += ") order by v.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);

		for (int i = 0; i < clientes.size(); i++) {
			query.setParameter("cli" + i, clientes.get(i));
		}

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}

	public static List<Venda> vendasNaoPagaDoCliente(Cliente cliente) {
		String stringQuery = "select v FROM Venda as v ";
		stringQuery += "WHERE v.paga = false and v.cliente = :cli"
				+ " order by v.total , v.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		query.setParameter("cli", cliente);

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) query.getResultList();
		return vendas;
	}
	
	
	public static List<Pagavel> pagavelCliente(Cliente cliente, Date diaInicio,
			Date diaFim){
		List<Pagavel> pag =  new ArrayList<Pagavel>();
		pag.addAll(vendasDoCliente(cliente, diaInicio, diaFim));
		pag.addAll(dividasDoCliente(cliente, diaInicio, diaFim));
		return pag;
	}
	
	public static List<Pagavel> pagavelNaoPagoDoCliente(Cliente cliente){
		List<Pagavel> pag =  new ArrayList<Pagavel>();
		pag.addAll(dividasNaoPagaDoCliente(cliente));
		pag.addAll(vendasNaoPagaDoCliente(cliente));
		return pag;
	}
	
	public static List<Pagavel> pagavelNaoPagoDosClientes(List<Cliente> clientes){
		List<Pagavel> pag =  new ArrayList<Pagavel>();
		pag.addAll(dividasNaoPagasDosClientes(clientes));
		pag.addAll(vendasNaoPagasDosClientes(clientes));
		return pag;
	}
	
	
	public static List<Divida> dividasNaoPagaDoCliente(Cliente cliente) {
		String stringQuery = "select d FROM Divida as d ";
		stringQuery += "WHERE d.paga = false and d.cliente = :cli"
				+ " order by d.total , d.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Divida.class);
		query.setParameter("cli", cliente);

		@SuppressWarnings("unchecked")
		List<Divida> dividas = (List<Divida>) query.getResultList();
		return dividas;
	}
	
	public static List<Divida> dividasNaoPagasDosClientes(List<Cliente> clientes) {
		String stringQuery = "select d FROM Divida as d ";
		stringQuery += "WHERE d.paga = false ";

		for (int i = 0; i < clientes.size(); i++) {
			if (i > 0) {
				stringQuery += "or d.cliente = :cli" + i + " ";
			} else {
				stringQuery += "and ( d.cliente = :cli" + i + " ";
			}
		}

		stringQuery += ") order by d.dia DESC ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Divida.class);

		for (int i = 0; i < clientes.size(); i++) {
			query.setParameter("cli" + i, clientes.get(i));
		}

		@SuppressWarnings("unchecked")
		List<Divida> dividas = (List<Divida>) query.getResultList();
		return dividas;
	}
	
	public static List<Divida> dividasDoCliente(Cliente cliente, Date diaInicio,
			Date diaFim) {
		
		Calendar di = Calendar.getInstance();
		di.setTime(diaInicio);
		di.set(Calendar.HOUR, 0);
		di.set(Calendar.MINUTE, 0);
		di.set(Calendar.SECOND, 00);
		
		Calendar df = Calendar.getInstance();
		df.setTime(diaFim);
		df.set(Calendar.HOUR, 23);
		df.set(Calendar.MINUTE, 59);
		df.set(Calendar.SECOND, 59);
		
		String stringQuery = "select d FROM Divida as d ";
		stringQuery += "WHERE d.cliente  = :cli and d.dia between :diaInicio and :diaFim"
				+ " order by d.dia, d.total DESC";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Divida.class);
		query.setParameter("cli", cliente);
		query.setParameter("diaInicio", di);
		query.setParameter("diaFim", df);

		@SuppressWarnings("unchecked")
		List<Divida> dividas = (List<Divida>) query.getResultList();
		return dividas;
	}
	

	@SuppressWarnings("unchecked")
	public static List<Venda> listVendas() {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em

		.createQuery("select venda from Venda venda");
		List<Venda> vendas = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return vendas;
	}

	public static Venda vendaId(int id) {
		String stringQuery = "select v FROM Venda as v WHERE v.id = :id";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		query.setParameter("id", id);

		Venda venda = (Venda) query.getSingleResult();
		return venda;
	}

	/*
	 * itens de venda
	 */

	@SuppressWarnings("unchecked")
	public static List<ItemDeVenda> recuperarLista() {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createQuery("select l from ItemDeVenda l");
		List<ItemDeVenda> LinhasDaVenda = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return LinhasDaVenda;
	}

	@SuppressWarnings("unchecked")
	public static List<ItemDeVenda> recuperarItensDaVenda(Venda v) {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createQuery("select l from ItemDeVenda as l where l.fk_venda = :idVenda");
		consulta.setParameter("idVenda", v.getId());
		List<ItemDeVenda> LinhasDaVenda = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return LinhasDaVenda;
	}

	public static ItemDeVenda itemDeVendaId(int id) {
		String stringQuery = "select i FROM ItemDeVenda as i WHERE i.id = :id";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery,
				ItemDeVenda.class);
		query.setParameter("id", id);

		ItemDeVenda item = (ItemDeVenda) query.getSingleResult();
		return item;
	}
	
	
	
	public static List<ItemDeVenda> itemDeVendaIdDaVenda(int idVenda) {
		String stringQuery = "select i FROM ItemDeVenda as i WHERE i.venda.id = :idVenda order by i.indice DESC";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery,
				ItemDeVenda.class);
		query.setParameter("idVenda", idVenda);

		@SuppressWarnings("unchecked")
		List<ItemDeVenda> itens = (List<ItemDeVenda>) query.getResultList();
		return itens;
	}

}
