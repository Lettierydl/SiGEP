package com.twol.sigep.controller.find;

import java.util.List;

import javax.persistence.Query;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.util.Persistencia;

public class FindProduto {

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueCodigoDeBarrasLike(
			String codigoDeBarras) {
		if (codigoDeBarras == null || codigoDeBarras.length() == 0)
			throw new IllegalArgumentException(
					"������������������ necess������������������rio um codigo de barras v������������������lido");
		codigoDeBarras = codigoDeBarras.replace('*', '%');
		if (codigoDeBarras.charAt(0) != '%') {
			codigoDeBarras = "%" + codigoDeBarras;
		}
		if (codigoDeBarras.charAt(codigoDeBarras.length() - 1) != '%') {
			codigoDeBarras = codigoDeBarras + "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();
		
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoLike(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma descri������������o v������lida");
		descricao = descricao.replace("%", "");
		descricao = descricao + "%";
		
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"SELECT o FROM Produto AS o WHERE LOWER(o.descricao) LIKE LOWER(:descricao) order by descricao",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueCodigoDeBarrasInicia(
			String codigoDeBarras) {
		if (codigoDeBarras == null || codigoDeBarras.length() == 0)
			throw new IllegalArgumentException(
					"������������������ necess������������������rio um codigo de barras v������������������lido");
		codigoDeBarras = codigoDeBarras.replace('*', '%');
		if (codigoDeBarras.charAt(0) != '%') {
			codigoDeBarras += "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();
		
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoInicia(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"������������������ necess������������������rio uma descri������������������������������������o v������������������lida");
		descricao = descricao.replace('*', '%');
		if (descricao.charAt(0) != '%') {
			descricao += "%";
		}
		Persistencia.restartConnection();
		Query q = Persistencia.em
				.createQuery(
						"select o from Produto as o where LOWER(o.descricao) LIKE LOWER(:descricao)",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		
		return produtos;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Produto> produtosQueDescricaoOuCodigoDeBarrasIniciam(
			String descricaoOuCodigo) {
		String stringQuery = "select p FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao) "
				+ "or LOWER(p.codigoDeBarras) LIKE LOWER(:codigoDeBarras) ";
		
		Persistencia.restartConnection();
		
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		query.setParameter("descricao", descricaoOuCodigo+ "%");
		query.setParameter("codigoDeBarras", descricaoOuCodigo+ "%");
		
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
	}

	public static Produto produtoComCodigoDeBarras(String codigo) {
		Persistencia.restartConnection();
		String stringQuery = "select p FROM Produto as p where LOWER(p.codigoDeBarras) LIKE LOWER(:codigo) ";
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		query.setParameter("codigo",codigo);
		
		Produto produto =  (Produto) query.getSingleResult();
		return produto;
	}
	
	public static Produto produtoComId(int id) {
		Persistencia.restartConnection();
		String stringQuery = "select p FROM Produto as p where p.id = :id ";
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		query.setParameter("id",id);
		
		Produto produto =  (Produto) query.getSingleResult();
		return produto;
	}

	public static List<Produto> todosProdutos() {
		String stringQuery = "select p FROM Produto as p ";
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		@SuppressWarnings("unchecked")
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
	}

	public static Produto produtoComCodigoEDescricao(String nomeOuCodigo) {
		Persistencia.restartConnection();
		String stringQuery = "select p FROM Produto as p where p.codigoDeBarras = :cod or p.descricao = :desc";
		Query query = Persistencia.em.createQuery(stringQuery, Produto.class);
		query.setParameter("desc", nomeOuCodigo);
		query.setParameter("cod", nomeOuCodigo);
		
		Produto produto =  (Produto) query.getSingleResult();
		return produto;
	}

	public static List<String> drecricaoProdutoQueIniciam(String descricao) {
		String stringQuery = "select p.descricao FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao) ";
		
		Persistencia.restartConnection();
		
		Query query = Persistencia.em.createQuery(stringQuery, String.class);
		query.setParameter("descricao", descricao+ "%");
		
		List<String> descricoes = (List<String>) query.getResultList();
		return descricoes;
	}
	
	public static List<String> codigoProdutoQueIniciam(String codigo) {
		String stringQuery = "select p.codigoBarras FROM Produto as p where LOWER(p.codigoBarras) LIKE LOWER(:codigoBarras) ";
		
		Persistencia.restartConnection();
		
		Query query = Persistencia.em.createQuery(stringQuery, String.class);
		query.setParameter("codigoBarras", codigo+ "%");
		
		List<String> codigos = (List<String>) query.getResultList();
		return codigos;
	}

}
