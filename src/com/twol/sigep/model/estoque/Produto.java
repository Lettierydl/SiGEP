package com.twol.sigep.model.estoque;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.exception.ConstraintViolationException;

import com.twol.sigep.model.exception.EntidadeJaPersistidaException;
import com.twol.sigep.model.pessoas.Representante;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findProdutoesByDescricaoLike", "findProdutoesByCategoria", "findProdutoesByCodigoDeBarrasLike", "findProdutoesByFabricante" })
@Table(name = "produto")
@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(nullable = false, unique = true, length = 13)
	private String codigoDeBarras;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false, precision = 2)
	private double valorDeCompra;

	/**
     */
	@Column(nullable = false, precision = 2)
	private double valorDeVenda;

	/**
     */
	@Column(nullable = false, precision = 4)
	private double quantidadeEmEstoque;

	/**
     */
	@Column(nullable = false, precision = 4)
	private double limiteMinimoEmEstoque;

	/**
     */
	@Enumerated(EnumType.STRING)
	private CategoriaProduto categoria;

	/**
     */
	@ManyToOne
	@JoinColumn(name = "fk_fabricante")
	private Representante fabricante;

	/**
     */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "produto")
	private List<Promocao> promocoesValidas = new ArrayList<Promocao>();

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorDeCompra() {
		return valorDeCompra;
	}

	public void setValorDeCompra(double valorDeCompra) {
		this.valorDeCompra = valorDeCompra;
	}

	public double getValorDeVenda() {
		return valorDeVenda;
	}

	public void setValorDeVenda(double valorDeVenda) {
		this.valorDeVenda = valorDeVenda;
	}

	public double getQuantidadeEmEstoque() {
		return quantidadeEmEstoque;
	}

	public void setQuantidadeEmEstoque(double quantidadeEmEstoque) {
		this.quantidadeEmEstoque = quantidadeEmEstoque;
	}

	public double getLimiteMinimoEmEstoque() {
		return limiteMinimoEmEstoque;
	}

	public void setLimiteMinimoEmEstoque(double limiteMinimoEmEstoque) {
		this.limiteMinimoEmEstoque = limiteMinimoEmEstoque;
	}

	public CategoriaProduto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}

	public Representante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Representante fabricante) {
		this.fabricante = fabricante;
	}

	public List<Promocao> getPromocoesValidas() {
		return promocoesValidas;
	}

	public void addPromocaoValida(Promocao p) {
		if (promocoesValidas == null) {
			promocoesValidas = new ArrayList<Promocao>();
		}
		promocoesValidas.add(p);
		p.setProduto(this);
	}

	public void removerPromocaoValida(Promocao p) {
		if (promocoesValidas == null) {
			return;
		}
		if (promocoesValidas.remove(p) && p.getProduto().equals(this)) {
			p.setProduto(this);
		}
	}

	public static void salvar(Produto p) throws EntidadeJaPersistidaException {
		if (p.getPromocoesValidas() != null
				&& !p.getPromocoesValidas().isEmpty()) {
			Persistencia.em.getTransaction().begin();
			Persistencia.em.persist(p.getPromocoesValidas().get(0));
			Persistencia.em.getTransaction().commit();
		} else {
			try {
				Persistencia.em.getTransaction().begin();
				Persistencia.em.merge(p);
				Persistencia.em.getTransaction().commit();
			} catch (ConstraintViolationException e) {
				throw new EntidadeJaPersistidaException("Produto");
			}finally{
				Persistencia.restartConnection();
			}
		}
	}

	public static void atualizar(Produto p) {
		Persistencia.em.getTransaction().begin();
		Persistencia.em.merge(p);
		Persistencia.em.getTransaction().commit();
	}

	public static void remover(Produto p) {
		Persistencia.em.getTransaction().begin();
		Persistencia.em.remove(p);
		Persistencia.em.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> recuperarLista() {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select produto from Produto produto");
		List<Produto> produtos = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return produtos;
	}

	public static Produto recuperarProduto(int idProduto) {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createQuery("select o from Produto as o WHERE o.id = :idProduto");
		consulta.setParameter("idProduto", idProduto);
		Produto produto = (Produto) consulta.getSingleResult();
		Persistencia.em.getTransaction().commit();
		return produto;
	}

	@Override
	public String toString() {
		return  descricao;
	}
	
	

}
