package com.twol.sigep.controller.find;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.util.Persistencia;

public class FindProduto {
	
	private  static EntityManager em;

	private static EntityManager getEntityManager() {
		//if (em != null && em.isOpen()) {
		//	em.close();
		//}
		if(em == null || !em.isOpen()){
			em = Persistencia.getEntityManager();
		}
		return em;
	}


	@SuppressWarnings("unchecked")
	public  List<Produto> produtosQueCodigoDeBarrasLike(
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
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();

		return produtos;
	}

	@SuppressWarnings("unchecked")
	public  List<Produto> produtosQueDescricaoLike(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"������ necess������rio uma descri������������o v������lida");
		descricao = descricao.replace("%", "");
		descricao = descricao + "%";

		
		Query q = getEntityManager()
				.createQuery(
						"SELECT o FROM Produto AS o WHERE LOWER(o.descricao) LIKE LOWER(:descricao) order by descricao",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public  List<Produto> produtosQueCodigoDeBarrasInicia(
			String codigoDeBarras) {
		if (codigoDeBarras == null || codigoDeBarras.length() == 0)
			throw new IllegalArgumentException(
					"������������������ necess������������������rio um codigo de barras v������������������lido");
		codigoDeBarras = codigoDeBarras.replace('*', '%');
		if (codigoDeBarras.charAt(0) != '%') {
			codigoDeBarras += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Produto as o where LOWER(o.codigoDeBarras) LIKE LOWER(:codigoDeBarras)",
						Produto.class);
		q.setParameter("codigoDeBarras", codigoDeBarras);
		List<Produto> produtos = q.getResultList();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public  List<Produto> produtosQueDescricaoInicia(String descricao) {
		if (descricao == null || descricao.length() == 0)
			throw new IllegalArgumentException(
					"������������������ necess������������������rio uma descri������������������������������������o v������������������lida");
		descricao = descricao.replace('*', '%');
		if (descricao.charAt(0) != '%') {
			descricao += "%";
		}
		
		Query q = getEntityManager()
				.createQuery(
						"select o from Produto as o where LOWER(o.descricao) LIKE LOWER(:descricao)  order by o.descricao",
						Produto.class);
		q.setParameter("descricao", descricao);
		List<Produto> produtos = q.getResultList();
		return produtos;
	}

	@SuppressWarnings("unchecked")
	public  List<Produto> produtosQueDescricaoOuCodigoDeBarrasIniciam(
			String descricaoOuCodigo) {
		String stringQuery = "select p FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao) "
				+ "or LOWER(p.codigoDeBarras) LIKE LOWER(:codigoDeBarras) order by p.descricao";

		

		Query query = getEntityManager().createQuery(stringQuery, Produto.class);
		query.setParameter("descricao", descricaoOuCodigo + "%");
		query.setParameter("codigoDeBarras", descricaoOuCodigo + "%");
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
	}

	public  Produto produtoComCodigoDeBarras(String codigo) {
		
		String stringQuery = "select p FROM Produto as p where LOWER(p.codigoDeBarras) LIKE LOWER(:codigo) ";
		Query query = getEntityManager().createQuery(stringQuery, Produto.class);
		query.setParameter("codigo", codigo);

		Produto produto = (Produto) query.getSingleResult();
		return produto;
	}

	public  Produto produtoComId(int id) {
		EntityManager em = getEntityManager();
		String stringQuery = "select p FROM Produto as p where p.id = :id  order by p.descricao";
		Query query = em.createQuery(stringQuery, Produto.class);
		query.setParameter("id", id);

		Produto produto = (Produto) query.getSingleResult();
		
		em.getTransaction().begin();
		em.refresh(produto);
		em.getTransaction().commit();
		
		return produto;
	}

	public  List<Produto> todosProdutos() {
		String stringQuery = "select p FROM Produto as p  order by p.descricao";
		
		Query query = getEntityManager().createQuery(stringQuery, Produto.class);
		@SuppressWarnings("unchecked")
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
	}
	
	
	public  List<Object[]> informacaoTodosProdutos() {
		String stringQuery = "SELECT id, descricao, codigoDeBarras, REPLACE( ROUND(valorDeVenda, 2), '.', ',')as valorDeVenda, CONCAT( REPLACE( ROUND(quantidadeEmEstoque, 3), '.', ',') , ' ',descricaoUnidade) as quantidadeEmEstoque  FROM produto order by descricao;";
		
		Query query = getEntityManager().createNativeQuery(stringQuery);
		Object o =null;
		try{
			o = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<Object[]> produtos = (List<Object[]>) o;
		return produtos;
	}

	public  Produto produtoComCodigoEDescricao(String nomeOuCodigo) {
		
		String stringQuery = "select p FROM Produto as p where p.codigoDeBarras = :cod or p.descricao = :desc  order by p.descricao";
		Query query = getEntityManager().createQuery(stringQuery, Produto.class);
		query.setParameter("desc", nomeOuCodigo);
		query.setParameter("cod", nomeOuCodigo);

		Produto produto = (Produto) query.getSingleResult();
		return produto;
	}

	public  List<String> drecricaoProdutoQueIniciam(String descricao) {
		String stringQuery = "select p.descricao FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao)  order by p.descricao";

		

		Query query = getEntityManager().createQuery(stringQuery, String.class);
		query.setParameter("descricao", descricao + "%");

		@SuppressWarnings("unchecked")
		List<String> descricoes = (List<String>) query.getResultList();
		return descricoes;
	}

	public  List<String> drecricaoProdutoQueIniciam(String descricao,
			int maxResult) {
		String stringQuery = "select p.descricao FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao)  order by p.descricao";

				Query query = getEntityManager().createQuery(stringQuery, String.class);
		query.setParameter("descricao", descricao + "%");

		query.setMaxResults(maxResult);

		@SuppressWarnings("unchecked")
		List<String> descricoes = (List<String>) query.getResultList();
		return descricoes;
	}

	public  List<String> codigoProdutoQueIniciam(String codigo) {
		String stringQuery = "select p.codigoBarras FROM Produto as p where LOWER(p.codigoBarras) LIKE LOWER(:codigoBarras)  order by p.descricao";

		Query query = getEntityManager().createQuery(stringQuery, String.class);
		query.setParameter("codigoBarras", codigo + "%");

		@SuppressWarnings("unchecked")
		List<String> codigos = (List<String>) query.getResultList();

		return codigos;
	}

	public  Produto produtoComDescricao(String descricao) {
		
		String stringQuery = "select p FROM Produto as p where p.descricao = :desc";
		Query query = getEntityManager().createQuery(stringQuery, Produto.class);
		query.setParameter("desc", descricao);

		Produto produto = (Produto) query.getSingleResult();

		return produto;
	}

	public  List<String> drecricaoEPrecoProdutoQueIniciam(
			String descricao, int maxResult) {
		String stringQuery = "select CONCAT(p.descricao, ' R$ ', REPLACE( ROUND(p.valorDeVenda, 2), '.', ',') ) FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao)  order by p.descricao";

		Query query = getEntityManager().createQuery(stringQuery, String.class);
		query.setParameter("descricao", descricao + "%");
		query.setMaxResults(maxResult);

		@SuppressWarnings("unchecked")
		List<String> descricoes = (List<String>) query.getResultList();

		return descricoes;

	}

	public  List<String> drecricaoEPrecoProdutoQueIniciam(String descricao) {
		String stringQuery = "select CONCAT(p.descricao, ' R$ ', REPLACE( ROUND(p.valorDeVenda, 2), '.', ',') ) FROM Produto as p where LOWER(p.descricao) LIKE LOWER(:descricao)  order by p.descricao";

		Query query = getEntityManager().createQuery(stringQuery, String.class);
		query.setParameter("descricao", descricao + "%");

		@SuppressWarnings("unchecked")
		List<String> descricoes = (List<String>) query.getResultList();

		return descricoes;

	}

}
