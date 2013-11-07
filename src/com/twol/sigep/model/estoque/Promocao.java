package com.twol.sigep.model.estoque;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

import com.twol.sigep.util.Persistencia;


@Entity(name = "promocao")
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
    @Temporal(TemporalType.TIMESTAMP)
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
    
	public static void salvar(Promocao p){
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
