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

import com.twol.sigep.model.Entidade;
import com.twol.sigep.util.Persistencia;

@Table(name = "promocao")
@Entity
public class Promocao extends Entidade {
	
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
    @Temporal(TemporalType.DATE)
    private Calendar dataDeFim;

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
	
	public Calendar getDataDeFim() {
		return dataDeFim;
	}

	public void setDataDeFim(Calendar dataDeFim) {
		this.dataDeFim = dataDeFim;
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
	
	public boolean isValida() {
		return this.quantidadeJaVendida >= this.quantidadeMaximaDeVendas
				&& this.dataDeInicio.compareTo(dataDeFim) > 0;
	}

	
	@SuppressWarnings("unchecked")
	public static List<Promocao> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select promocao from Promocao promocao");
		List<Promocao> promocoes = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return promocoes;
    }

	public double calcularValorDoDesconto(double quantidadeVendida) {
		double quantidade = 
				(quantidadeVendida > (quantidadeMaximaDeVendas - quantidadeJaVendida))
				?(quantidadeMaximaDeVendas - quantidadeJaVendida):quantidadeVendida;
		return valorDoDesconto * quantidade;
	}

    
}
