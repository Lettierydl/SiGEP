package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.*;

import com.twol.sigep.util.Persistencia;

@Entity(name="telefone")
public class Telefone {
	
	
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
    
    
	
	public static void salvar(Telefone e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void atualizar(Telefone e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Telefone e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Telefone> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				
				.createNamedQuery("select telefone from Telefone telefone");
		List<Telefone> telefones = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return telefones;
    }
    
}
