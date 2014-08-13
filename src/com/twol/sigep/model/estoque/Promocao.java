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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.infinispan.factories.annotations.DefaultFactoryFor;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.model.exception.PromocaoInvalida;
import com.twol.sigep.model.exception.PromocaoValidaJaExistente;
import com.twol.sigep.util.Persistencia;

@Table(name = "promocao")
@Entity
public class Promocao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;	
	
	/**
     */
    @Temporal(TemporalType.DATE)
    private Calendar dataDeInicio;
    
    /**
     */
    @Temporal(TemporalType.DATE)
    private Calendar dataDeFim;

    /**
     * valor do desconto sobre o valor do produto
     * Exemplo: produto custa 5,50 e o valor do desconto é 0,50
     * entao o valor com o desconto do produto é 5,00
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

    /**
    */
    @ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(updatable=true)
	@ForeignKey(name = "promocao_valida")
    private Produto produto;
    
    
    @Column(nullable = false)
    private boolean ativa = true;
    
    
    public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
    
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

	public void setProduto(Produto produto) throws PromocaoValidaJaExistente, PromocaoInvalida {
		this.produto = produto;
		this.produto.addPromocaoValida(this);
	}
	
	public boolean isValida() {
		return (ativa) && (this.quantidadeJaVendida < this.quantidadeMaximaDeVendas
				&& Calendar.getInstance().compareTo(dataDeFim) > 0);
	}
	
	public double calcularValorDoDesconto(double quantidadeVendida) {
		double quantidade = 
				(quantidadeVendida > (quantidadeMaximaDeVendas - quantidadeJaVendida))
				?(quantidadeMaximaDeVendas - quantidadeJaVendida):quantidadeVendida;
		return valorDoDesconto * quantidade;
	}
	
	public void desativar(){
		this.ativa = false;
	}
	
	public void ativar(){
		this.ativa = true;
	}

	
	@SuppressWarnings("unchecked")
	public static List<Promocao> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select promocao from Promocao promocao");
		List<Promocao> promocoes = consulta.getResultList();
		return promocoes;
    }
	
	@SuppressWarnings("unchecked")
	public static Promocao recuperarPromocao(int id){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select promocao from Promocao promocao where promocao.id = :id");
		consulta.setParameter("id", id);
		Promocao pro = (Promocao) consulta.getSingleResult();
		return pro;
    }

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Promocao other = (Promocao) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}
