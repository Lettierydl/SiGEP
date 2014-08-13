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

import com.twol.sigep.util.Persistencia;

@Table(name = "telefone")
@Entity
public class Telefone{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
    /**
     */
    @Column(nullable = false, length = 2)
    private String ddd;

    /**
     */
    @Column(nullable = false, length = 9)
    private String telefone;

    /**
     */
    @Column(nullable = false)
    private String operadora;
    
    
    
    public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
    
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
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createNamedQuery("select telefone from Telefone telefone");
		List<Telefone> telefones = consulta.getResultList();
		return telefones;
    }
	
	public static Telefone recuperarTelefoneId(int id) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select t from Telefone as t where t.id = :id", Telefone.class);
		consulta.setParameter("id", id);
		Telefone telefone = (Telefone) consulta.getSingleResult();
		return telefone;
	}

	@Override
	public String toString() {
		return "("+ddd+") "+ telefone.substring(0, 4)+"-"+
				telefone.substring(4);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result
				+ ((operadora == null) ? 0 : operadora.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
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
		Telefone other = (Telefone) obj;
		if (ddd == null) {
			if (other.ddd != null)
				return false;
		} else if (!ddd.equals(other.ddd))
			return false;
		if (operadora == null) {
			if (other.operadora != null)
				return false;
		} else if (!operadora.equals(other.operadora))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}

	
    
}
