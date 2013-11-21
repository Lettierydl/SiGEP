package com.twol.sigep.model.estoque;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.twol.sigep.util.Persistencia;

public class FinderProduto {

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueCodigoDeBarrasLike(
			String codigoDeBarras) {
		if (codigoDeBarras == null || codigoDeBarras.length() == 0)
			throw new IllegalArgumentException(
					"É necessário um codigo de barras válido");
		codigoDeBarras = codigoDeBarras.replace('*', '%');
		if (codigoDeBarras.charAt(0) != '%') {
			codigoDeBarras = "%" + codigoDeBarras;
		}
		if (codigoDeBarras.charAt(codigoDeBarras.length() - 1) != '%') {
			codigoDeBarras = codigoDeBarras + "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoLike(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"É necessário uma descrição válida");
		descricao = descricao.replace('*', '%');
		if (descricao.charAt(0) != '%') {
			descricao = "%" + descricao;
		}
		if (descricao.charAt(descricao.length() - 1) != '%') {
			descricao = descricao + "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"SELECT o FROM Produto AS o WHERE LOWER(o.descricao) LIKE LOWER(:descricao)",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueCodigoDeBarrasInicia(
			String codigoDeBarras) {
		if (codigoDeBarras == null || codigoDeBarras.length() == 0)
			throw new IllegalArgumentException(
					"É necessário um codigo de barras válido");
		codigoDeBarras = codigoDeBarras.replace('*', '%');
		if (codigoDeBarras.charAt(0) != '%') {
			codigoDeBarras += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoInicia(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"É necessário uma descrição válida");
		descricao = descricao.replace('*', '%');
		if (descricao.charAt(0) != '%') {
			descricao += "%";
		}
		Persistencia.em.getTransaction().begin();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.descricao) LIKE LOWER(:descricao)",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		Persistencia.em.getTransaction().commit();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoOuCodigoDeBarrasIniciam(
			String descricao, String codigoDeBarras) {
		String stringQuery = "select p FROM Produto as p ";
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
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		for (Map.Entry<String, String> valor : valores.entrySet()) {
			query.setParameter(valor.getKey(), valor.getValue());
		}
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
	}

}
