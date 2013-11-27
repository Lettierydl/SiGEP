package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.util.Persistencia;

@Table(name = "telefone")
@Entity
public class Telefone extends Entidade{
	
	
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
    @Column(nullable = false, length = 2)
    private String ddd;

    /**
     */
    @Column(nullable = false, length = 8)
    private String telefone;

    /**
     */
    @Column(nullable = false)
    private String operadora;

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}
    
	
	@SuppressWarnings("unchecked")
	public static List<Telefone> recuperarLista(){
		Query consulta = Persistencia.em
				.createNamedQuery("select telefone from Telefone telefone");
		List<Telefone> telefones = consulta.getResultList();
		return telefones;
    }

	@Override
	public String toString() {
		return "("+ddd+") "+ telefone.substring(0, 4)+"-"+
				telefone.substring(4);
	}
	
	
    
}
