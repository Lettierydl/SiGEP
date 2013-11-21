package com.twol.sigep.model.vendas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

public class FinderVenda {

	@SuppressWarnings("unchecked")
	public static List<Venda> VendasQueNomeLike(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"É necessário um nome válido");
		nome = nome.replace('*', '%');
		if (nome.charAt(0) != '%') {
			nome = "%" + nome;
		}
		if (nome.charAt(nome.length() - 1) != '%') {
			nome += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Venda as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Venda.class);
		q.setParameter("nome", nome);
		List<Venda> Vendas = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> VendasQueCPFLike(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"É necessário uma CPF válido");
		cpf = cpf.replace('*', '%');
		if (cpf.charAt(0) != '%') {
			cpf = "%" + cpf;
		}
		if (cpf.charAt(cpf.length() - 1) != '%') {
			cpf += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"SELECT o FROM Venda AS o WHERE LOWER(o.cpf) LIKE LOWER(:cpf)",
						Venda.class);
		q.setParameter("cpf", cpf);
		List<Venda> Vendas = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> VendasQueNomeInicia(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"É necessário um nome válido");
		nome = nome.replace('*', '%');
		if (nome.charAt(0) != '%') {
			nome += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Venda as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Venda.class);
		q.setParameter("nome", nome);
		List<Venda> Vendas = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> VendasQueCPFInicia(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"É necessário uma CPF válida");
		cpf = cpf.replace('*', '%');
		if (cpf.charAt(0) != '%') {
			cpf += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Venda as o where LOWER(o.cpf) LIKE LOWER(:cpf)",
						Venda.class);
		q.setParameter("cpf", cpf);
		List<Venda> Vendas = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return Vendas;
	}

	@SuppressWarnings("unchecked")
	public static List<Venda> VendasQueDescricaoOuCodigoDeBarrasIniciam(
			String descricao, String codigoDeBarras) {
		String stringQuery = "select p FROM Venda as p ";
		Map<String, String> valores = new HashMap<String, String>();
		if (descricao != null) {
			stringQuery += "where LOWER(p.descricao) LIKE LOWER(:descricao) ";
			valores.put("descricao", descricao + "%");
			if (codigoDeBarras != null) {
				stringQuery += "and LOWER(p.codigoDeBarras) LIKE LOWER(:codigoDeBarras) ";
				valores.put("codigoDeBarras",codigoDeBarras + "%");
			}
		} else if (codigoDeBarras != null) {
			stringQuery += "where LOWER(p.codigoDeBarras) LIKE LOWER(:codigoDeBarras) ";
			valores.put("codigoDeBarras", codigoDeBarras + "%");
		}
		Query query = Persistencia.em.createQuery(stringQuery, Venda.class);
		for (Map.Entry<String, String> valor : valores.entrySet()) {
			query.setParameter(valor.getKey(), valor.getValue());
		}
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
