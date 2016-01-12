package com.twol.sigep.model.pessoas;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.Entidade;


//@RooJpaActiveRecord(finders = { "findDependentesByNomeEquals", "findDependentesByNomeLike" })
@Table(name = "dependente")
@Entity
public class Dependente extends Entidade{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
    /**
     */
	@Column(nullable = false)
    private String nome;
	
	@ManyToOne
	@JoinColumn(updatable=true)
	@ForeignKey(name = "dependentes_do_cliente")
	@OnDelete(action=OnDeleteAction.NO_ACTION)
	private Cliente cliente;
	
	public void setCliente(Cliente cliente){
		this.cliente = cliente;
	}
	
	public Cliente getCliente(){
		return this.cliente;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/*
	@SuppressWarnings("unchecked")
	public static List<Dependente> recuperarLista() {
		Persistencia.restartConnection();
		Query consulta = Persistencia.getEntityManager()
				.createNamedQuery("select dependente from Dependente dependente");
		List<Dependente> dependentes = consulta.getResultList();
		return dependentes;
	}
	*/
}
