package com.twol.sigep.controller.find;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

public class FindFuncionario {
	
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
	public  List<Funcionario> funcionariosQueNomeLike(String nome) {
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
						"select o from Funcionario as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome.replace(" ", "%"));
		List<Funcionario> funcionarios = q.getResultList();


		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public  List<Funcionario> funcionariosQueLoginLike(String login) {
		if (login == null || login.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma Login v������lido");
		login = login.replace('*', '%');
		if (login.charAt(0) != '%') {
			login = "%" + login;
		}
		if (login.charAt(login.length() - 1) != '%') {
			login += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"SELECT o FROM Funcionario AS o WHERE LOWER(o.login) LIKE LOWER(:login)",
						Funcionario.class);
		q.setParameter("login", login.replace(" ", "%"));
		List<Funcionario> funcionarios = q.getResultList();

		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public  List<Funcionario> funcionariosQueNomeInicia(String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		nome = nome.replace('*', '%');
		if (nome.charAt(0) != '%') {
			nome += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Funcionario as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome);
		List<Funcionario> funcionarios = q.getResultList();

		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public  List<Funcionario> funcionariosQueLoginInicia(String login) {
		if (login == null || login.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma CPF v������lida");
		login = login.replace('*', '%');
		if (login.charAt(0) != '%') {
			login += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Funcionario as o where LOWER(o.login) LIKE LOWER(:login)",
						Funcionario.class);
		q.setParameter("login", login);
		List<Funcionario> Funcionarios = q.getResultList();

		return Funcionarios;
	}

	public  List<String> nomeFuncionariosQueNomeInicia(String nome) {
		String stringQuery = "select c.nome FROM Funcionario as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) ";

		
		Query query = getEntityManager().createQuery(stringQuery, String.class);

		query.setParameter("nome", nome.replace(" ", "%") + "%");

		@SuppressWarnings("unchecked")
		List<String> nomes = (List<String>) query.getResultList();

		return nomes;
	}

	@SuppressWarnings("unchecked")
	public  List<Funcionario> listFuncionarios() {
		
		Query consulta = getEntityManager()
				.createQuery("select Funcionario from Funcionario Funcionario order by nome");
		List<Funcionario> Funcionarios = consulta.getResultList();

		return Funcionarios;
	}

	public  Funcionario funcionarioComId(int idFuncionario) {
		
		Query consulta = getEntityManager().createQuery(
				"select c from Funcionario as c where c.id = :idFuncionario ",
				Funcionario.class);
		consulta.setParameter("idFuncionario", idFuncionario);
		Funcionario c = (Funcionario) consulta.getSingleResult();

		return c;
	}

	public  Funcionario funcionarioComNome(String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Funcionario as o where LOWER(o.nome) = LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome);
		Funcionario Funcionario = (Funcionario) q.getSingleResult();

		return Funcionario;
	}
	
	public  Funcionario funcionarioComCPF(String cpf) {
		if (cpf == null || cpf.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Funcionario as o where o.cpf = :cpf",
						Funcionario.class);
		q.setParameter("cpf", cpf);
		Funcionario Funcionario = (Funcionario) q.getSingleResult();

		return Funcionario;
	}
	
	public  Funcionario funcionarioComLoginESenha
	(String login, String senha){
		
		Query consulta = getEntityManager()
				.createQuery("select f from Funcionario as f where f.login = :lo " +
						"and f.senha = :se");
		consulta.setParameter("lo", login);
		consulta.setParameter("se", senha);
		Funcionario fuc= (Funcionario) consulta.getSingleResult();

		return fuc;
    }
	
	public  Funcionario funcionarioComLogin
	(String login){
		
		Query consulta = getEntityManager()
				.createQuery("select f from Funcionario as f where f.login = :lo");
		consulta.setParameter("lo", login);
		Funcionario fuc= (Funcionario) consulta.getSingleResult();

		return fuc;
    }


}
