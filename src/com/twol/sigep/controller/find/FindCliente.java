package com.twol.sigep.controller.find;

import java.util.List;

import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.util.Persistencia;

public class FindCliente {

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueNomeLike(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
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
		q.setParameter("nome", nome.replace(" ", "%"));
		List<Cliente> clientes = q.getResultList();
		
		return clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueCPFLike(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma CPF v������lido");
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
		q.setParameter("cpf", cpf.replace(" ", "%"));
		List<Cliente> clientes = q.getResultList();
		
		return clientes;
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> clientesQueNomeInicia(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
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
					"������ necess������rio uma CPF v������lida");
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
	public static List<Cliente> clientesQueNomeOuCPFIniciam(
			String nomeOuCpf) {
		String stringQuery = "select c FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) or " +
							 "LOWER(c.cpf) LIKE LOWER(:cpf)";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Cliente.class);
		
		query.setParameter("nome", nomeOuCpf.replace(" ", "%")+"%");
		query.setParameter("cpf", nomeOuCpf.replace(" ", "%")+"%");
		
		List<Cliente> Clientes = (List<Cliente>) query.getResultList();
		return Clientes;
	}

	public static List<String> nomeClientesQueNomeInicia(String nome) {
		String stringQuery = "select c.nome FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) ";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, String.class);
		
		query.setParameter("nome", nome.replace(" ", "%")+"%");
		
		@SuppressWarnings("unchecked")
		List<String> nomes = (List<String>) query.getResultList();
		return nomes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Cliente> listCelientes() {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select cliente from Cliente cliente order by nome");
		List<Cliente> clientes = consulta.getResultList();
		return clientes;
	}

	public static Cliente clienteComId(int idCliente) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em.createQuery(
				"select c from Cliente as c where c.id = :idCliente ",
				Cliente.class);
		consulta.setParameter("idCliente", idCliente);
		Cliente c = (Cliente) consulta.getSingleResult();
		return c;
	}
	
	public static Cliente clientesComNome(
			String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) = LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		Cliente cliente = (Cliente) q.getSingleResult();
		return cliente;
	}
	
	public static Cliente clientesComCPF(
			String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Cliente as o where LOWER(o.cpf) = LOWER(:cpf)",
						Cliente.class);
		q.setParameter("cpf", cpf);
		Cliente cliente = (Cliente) q.getSingleResult();
		return cliente;
	}


}
