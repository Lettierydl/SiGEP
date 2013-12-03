package com.twol.sigep.model.pessoas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.twol.sigep.util.Persistencia;

public class FinderCliente {

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueNomeLike(
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
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		List<Cliente> clientes = q.getResultList();
		
		return clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueCPFLike(String cpf) {
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
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"SELECT o FROM Cliente AS o WHERE LOWER(o.cpf) LIKE LOWER(:cpf)",
						Cliente.class);
		q.setParameter("cpf", cpf);
		List<Cliente> clientes = q.getResultList();
		
		return clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueNomeInicia(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"É necessário um nome válido");
		nome = nome.replace('*', '%');
		if (nome.charAt(0) != '%') {
			nome += "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		List<Cliente> Clientes = q.getResultList();
		
		return Clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueCPFInicia(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"É necessário uma CPF válida");
		cpf = cpf.replace('*', '%');
		if (cpf.charAt(0) != '%') {
			cpf += "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Cliente as o where LOWER(o.cpf) LIKE LOWER(:cpf)",
						Cliente.class);
		q.setParameter("cpf", cpf);
		List<Cliente> Clientes = q.getResultList();
		
		return Clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueDescricaoOuCodigoDeBarrasIniciam(
			String descricao, String codigoDeBarras) {
		String stringQuery = "select p FROM Cliente as p ";
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
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Cliente.class);
		for (Map.Entry<String, String> valor : valores.entrySet()) {
			query.setParameter(valor.getKey(), valor.getValue());
		}
		List<Cliente> Clientes = (List<Cliente>) query.getResultList();
		return Clientes;
	}

}
