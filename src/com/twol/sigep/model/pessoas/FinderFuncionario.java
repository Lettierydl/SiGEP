package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.Query;

import com.twol.sigep.util.Persistencia;

public class FinderFuncionario {

	@SuppressWarnings("unchecked")
	public static List<Funcionario> funcionariosQueNomeLike(String nome) {
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
						"select o from Funcionario as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome.replace(" ", "%"));
		List<Funcionario> funcionarios = q.getResultList();

		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public static List<Funcionario> funcionariosQueLoginLike(String login) {
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
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"SELECT o FROM Funcionario AS o WHERE LOWER(o.login) LIKE LOWER(:login)",
						Funcionario.class);
		q.setParameter("login", login.replace(" ", "%"));
		List<Funcionario> funcionarios = q.getResultList();

		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public static List<Funcionario> funcionariosQueNomeInicia(String nome) {
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
						"select o from Funcionario as o where LOWER(o.nome) LIKE LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome);
		List<Funcionario> funcionarios = q.getResultList();

		return funcionarios;
	}

	@SuppressWarnings("unchecked")
	public static List<Funcionario> funcionariosQueLoginInicia(String login) {
		if (login == null || login.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma CPF v������lida");
		login = login.replace('*', '%');
		if (login.charAt(0) != '%') {
			login += "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Funcionario as o where LOWER(o.login) LIKE LOWER(:login)",
						Funcionario.class);
		q.setParameter("login", login);
		List<Funcionario> Funcionarios = q.getResultList();

		return Funcionarios;
	}

	public static List<String> nomeFuncionariosQueNomeInicia(String nome) {
		String stringQuery = "select c.nome FROM Funcionario as c ";
		stringQuery += "where LOWER(c.nome) LIKE LOWER(:nome) ";

		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, String.class);

		query.setParameter("nome", nome.replace(" ", "%") + "%");

		@SuppressWarnings("unchecked")
		List<String> nomes = (List<String>) query.getResultList();
		return nomes;
	}

	@SuppressWarnings("unchecked")
	public static List<Funcionario> listFuncionarios() {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select Funcionario from Funcionario Funcionario order by nome");
		List<Funcionario> Funcionarios = consulta.getResultList();
		return Funcionarios;
	}

	public static Funcionario funcionarioComId(int idFuncionario) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em.createQuery(
				"select c from Funcionario as c where c.id = :idFuncionario ",
				Funcionario.class);
		consulta.setParameter("idFuncionario", idFuncionario);
		Funcionario c = (Funcionario) consulta.getSingleResult();
		return c;
	}

	public static Funcionario funcionarioComNome(String nome) {
		if (nome == null || nome.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio um nome v������lido");
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Funcionario as o where LOWER(o.nome) = LOWER(:nome)",
						Funcionario.class);
		q.setParameter("nome", nome);
		Funcionario Funcionario = (Funcionario) q.getSingleResult();
		return Funcionario;
	}
	
	public static Funcionario funcionarioComLoginESenha
	(String login, String senha){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select f from Funcionario as f where f.login = :lo " +
						"and f.senha = :se");
		consulta.setParameter("lo", login);
		consulta.setParameter("se", senha);
		Funcionario fuc= (Funcionario) consulta.getSingleResult();
		return fuc;
    }
	
	public static Funcionario funcionarioComLogin
	(String login){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select f from Funcionario as f where f.login = :lo");
		consulta.setParameter("lo", login);
		Funcionario fuc= (Funcionario) consulta.getSingleResult();
		return fuc;
    }

}
