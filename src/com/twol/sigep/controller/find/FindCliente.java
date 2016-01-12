package com.twol.sigep.controller.find;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.util.Persistencia;

public class FindCliente {

	private  EntityManager em;

	private  EntityManager getEntityManager() {
		/*if (em != null && em.isOpen()) {
			em.close();
		}*/
		if(em == null || !em.isOpen()){
			em = Persistencia.getEntityManager();
		}
		
		return em;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> clientesQueNomeLike(String nome) {
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
		Query q = getEntityManager()
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		List<Cliente> clientes = q.getResultList();

		return clientes;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> clientesQueCPFLike(String cpf) {
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
		
		
		Query q = getEntityManager()
				.createQuery(
						"SELECT o FROM Cliente AS o WHERE LOWER(o.cpf) LIKE LOWER(:cpf)",
						Cliente.class);
		q.setParameter("cpf", cpf.replace(" ", "%"));
		List<Cliente> clientes = q.getResultList();
		
		return clientes;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> clientesQueNomeInicia(String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		nome = nome.replace('*', '%');
		if (nome.charAt(0) != '%') {
			nome += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		List<Cliente> Clientes = q.getResultList();
		
		return Clientes;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> clientesQueCPFInicia(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma CPF v������lida");
		cpf = cpf.replace('*', '%');
		if (cpf.charAt(0) != '%') {
			cpf += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Cliente as o where LOWER(o.cpf) LIKE LOWER(:cpf)",
						Cliente.class);
		q.setParameter("cpf", cpf);
		List<Cliente> Clientes = q.getResultList();
		
		return Clientes;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> clientesQueNomeOuCPFIniciam(String nomeOuCpf) {
		String stringQuery = "select c FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) or "
				+ "LOWER(c.cpf) LIKE LOWER(:cpf)";

		
		Query query = getEntityManager().createQuery(stringQuery, Cliente.class);
		
		nomeOuCpf +="%";
		query.setParameter("nome", nomeOuCpf);
		query.setParameter("cpf", nomeOuCpf);

		List<Cliente> Clientes = (List<Cliente>) query.getResultList();
		
		
		return Clientes;
	}

	public  List<Cliente> clientesQueNomeOuCPFIniciam(String nomeOuCpf,
			int maxResult) {
		String stringQuery = "select c FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) or "
				+ "LOWER(c.cpf) LIKE LOWER(:cpf)";

		
		Query query = getEntityManager().createQuery(stringQuery, Cliente.class);
		
		nomeOuCpf +="%";
		query.setParameter("nome", nomeOuCpf);
		query.setParameter("cpf", nomeOuCpf);

		query.setMaxResults(maxResult);

		@SuppressWarnings("unchecked")
		List<Cliente> Clientes = (List<Cliente>) query.getResultList();
		
		
		
		return Clientes;
	}

	public  Cliente clientesQueNomeOuCPFIqualA(String nomeOuCpf) {
		String stringQuery = "select c FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) = LOWER(:nome) or "
				+ "LOWER(c.cpf) = LOWER(:cpf)";

		
		Query query = getEntityManager().createQuery(stringQuery, Cliente.class);

		query.setParameter("nome", nomeOuCpf);
		query.setParameter("cpf", nomeOuCpf);

		Cliente cliente = (Cliente) query.getSingleResult();
		
		
		
		return cliente;
	}

	public  List<String> nomeClientesQueNomeInicia(String nome) {
		String stringQuery = "select c.nome FROM Cliente as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) ";

		
		Query query = getEntityManager().createQuery(stringQuery, String.class);

		nome += "%";
		query.setParameter("nome", nome);
		
		@SuppressWarnings("unchecked")
		List<String> nomes = (List<String>) query.getResultList();
		
		
		
		return nomes;
	}

	@SuppressWarnings("unchecked")
	public  List<Cliente> listCelientes() {
		
		Query consulta = getEntityManager()
				.createQuery("select cliente from Cliente cliente order by nome");
		List<Cliente> clientes = consulta.getResultList();
		
		
		
		return clientes;
	}

	public  Cliente clienteComId(int idCliente) {
		
		Query consulta = getEntityManager().createQuery(
				"select c from Cliente as c where c.id = :idCliente ",
				Cliente.class);
		consulta.setParameter("idCliente", idCliente);
		Cliente c = (Cliente) consulta.getSingleResult();
		
		
		
		return c;
	}

	public  Cliente clienteComNome(String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Cliente as o where LOWER(o.nome) = LOWER(:nome)",
						Cliente.class);
		q.setParameter("nome", nome);
		Cliente cliente = (Cliente) q.getSingleResult();
		
		
		
		return cliente;
	}

	public  Cliente clienteComCPF(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		
		Query q = getEntityManager().createQuery(
				"select o from Cliente as o where LOWER(o.cpf) = LOWER(:cpf)",
				Cliente.class);
		q.setParameter("cpf", cpf);
		Cliente cliente = (Cliente) q.getSingleResult();
		
		
		
		return cliente;
	}
	
	/*Dependente*/

	public Dependente dependenteComId(int id) {
		Query consulta = getEntityManager().createQuery(
				"select d from Dependente as d where d.id = :idDependente ",
				Cliente.class);
		consulta.setParameter("idDependente", id);
		Dependente d = (Dependente) consulta.getSingleResult();
		return d;
	}

	public List<String> listNomeDependentes(int idCliente) {
		String stringQuery = "select d.nome FROM Dependente as d ";
		stringQuery += "where d.cliente_id = :idCliente ;";

		
		Query query = getEntityManager().createNativeQuery(stringQuery);

		query.setParameter("idCliente", idCliente);
		
		@SuppressWarnings("unchecked")
		List<String> nomes = (List<String>) query.getResultList();
		
		return nomes;
	}

	public List<Dependente> listDependentes(Cliente c) {
		String stringQuery = "select d FROM Dependente as d ";
		stringQuery += "where d.cliente = :bCliente  ";

		
		Query query = getEntityManager().createQuery(stringQuery, Dependente.class);

		query.setParameter("bCliente", c);
		
		@SuppressWarnings("unchecked")
		List<Dependente> dependentes = (List<Dependente>) query.getResultList();
		
		return dependentes;
	}

	public Dependente dependenteComNomeClietne(String nomeDependente, Cliente c) {
		Query consulta = getEntityManager().createQuery(
				"select d from Dependente as d where d.nome = :nome and d.cliente = :cli ",
				Dependente.class);
		consulta.setParameter("nome", nomeDependente);
		consulta.setParameter("cli", c);
		Dependente d = (Dependente) consulta.getSingleResult();
		return d;
	}

}
