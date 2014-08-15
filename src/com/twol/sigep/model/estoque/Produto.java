package com.twol.sigep.model.estoque;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.twol.sigep.model.exception.PromocaoInvalida;
import com.twol.sigep.model.exception.PromocaoValidaJaExistente;

//@RooJpaActiveRecord(finders = { "findProdutoesByDescricaoLike", "findProdutoesByCategoria", "findProdutoesByCodigoDeBarrasLike", "findProdutoesByFabricante" })
@Table(name = "produto")
@Entity
public class Produto{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	@Column(nullable = false, unique = true, length = 13)
	private String codigoDeBarras;

	@Column(nullable = false)
	private String descricao;
	
	
	@Enumerated(EnumType.STRING)
	private UnidadeProduto descricaoUnidade;
	

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
	private CategoriaProduto categoria = CategoriaProduto.Outra;

	
	
	
	/**
     
	@ManyToOne
	@JoinColumn(updatable=true)
	@ForeignKey(name = "representante_do_produto")
	private Representante fabricante;


	/**
    */
	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER,mappedBy = "produto")
	@Fetch(FetchMode.SUBSELECT)  
	private List<Promocao> promocoes = new ArrayList<Promocao>();
	
	
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

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

	public UnidadeProduto getDescricaoUnidade() {
		return descricaoUnidade;
	}

	public void setDescricaoUnidade(UnidadeProduto descricaoUnidade) {
		this.descricaoUnidade = descricaoUnidade;
	}

	/*
	public Representante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Representante fabricante) {
		this.fabricante = fabricante;
	}
	*/

	public List<Promocao> getPromocoes() {
		return promocoes;
	}
	public Promocao getPromocaoValida() {
		for(Promocao pr : this.getPromocoes()){
			if(pr.isValida()){
				return pr;
			}
		}
		return null;
	}

	//apenas quem deve usar esse metodo é o Promocao.setProduto(Produto)
	void addPromocaoValida(Promocao p) throws PromocaoValidaJaExistente, PromocaoInvalida {
		if (promocoes == null) {
			promocoes = new ArrayList<Promocao>();
		}
		if(!p.isValida()){
			throw new PromocaoInvalida(p);
		}
		if(this.getPromocaoValida()!= null){
			throw new PromocaoValidaJaExistente(p);
		}
		promocoes.add(p);
		//p.setProduto(this);
	}

	//apenas quem deve usar esse metodo é o destry(promocao)
	public void removerPromocao(Promocao p) {
		if (getPromocoes() == null) {
			return;
		}
		promocoes.get(0).equals(p);
		promocoes.remove(p);
	}
	/*
	@Override
	protected List<?> getListEntidadeRelacionada(){
		return super.getListEntidadeRelacionada();
	}

	@SuppressWarnings("unchecked")
	public static List<Produto> recuperarLista() {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select p from Produto as p order by descricao");
		List<Produto> produtos = consulta.getResultList();
		return produtos;
	}

	public static Produto recuperarProduto(int idProduto) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select o from Produto as o WHERE o.id = :idProduto");
		consulta.setParameter("idProduto", idProduto);
		Produto produto = (Produto) consulta.getSingleResult();
		return produto;
	}
	*/
	@Override
	public String toString() {
		return  descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	

}
