package com.twol.sigep.model.estoque;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.util.Persistencia;

@Table(name = "promocao")
@Entity
public class Promocao {
	
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
	
	
    /**
     */
    @Temporal(TemporalType.DATE)
    private Calendar dataDeInicio;

    /**
     */
    @Column(nullable = false, precision = 2)
    private double valorDoDesconto;

    /**
     */
    @Column(nullable = true, precision = 4)
    private double quantidadeMaximaDeVendas;

    /**
     */
    @Column(nullable = true, precision = 4)
    private double quantidadeJaVendida;

    @ManyToOne(cascade = CascadeType.ALL)
    private Produto produto;
    
    
	public Calendar getDataDeInicio() {
		return dataDeInicio;
	}

	public void setDataDeInicio(Calendar dataDeInicio) {
		this.dataDeInicio = dataDeInicio;
	}

	public double getValorDoDesconto() {
		return valorDoDesconto;
	}

	public void setValorDoDesconto(double valorDoDesconto) {
		this.valorDoDesconto = valorDoDesconto;
	}

	public double getQuantidadeMaximaDeVendas() {
		return quantidadeMaximaDeVendas;
	}

	public void setQuantidadeMaximaDeVendas(double quantidadeMaximaDeVendas) {
		this.quantidadeMaximaDeVendas = quantidadeMaximaDeVendas;
	}

	public double getQuantidadeJaVendida() {
		return quantidadeJaVendida;
	}

	public void setQuantidadeJaVendida(double quantidadeJaVendida) {
		this.quantidadeJaVendida = quantidadeJaVendida;
	}
	
	public Produto getProduto() {
		return produto;
	}

	void setProduto(Produto produto) {
		this.produto = produto;
	}

	/*
	 * Não precisa salvar promocao,
	 * apenas add na lista de promocoes validas do produto
	 */
	static void salvar(Promocao p){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(p);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void atualizar(Promocao p){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(p);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Promocao p){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(p);
    	Persistencia.em.getTransaction().commit();
    }
	
	
	/*
	 * Não precisa salvar Promocao, apenas adicionar na lista de Produto
	 */
	@SuppressWarnings("unchecked")
	public static List<Promocao> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select promocao from Promocao promocao");
		List<Promocao> promocoes = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return promocoes;
    }
    
}
