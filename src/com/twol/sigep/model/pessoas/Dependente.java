package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.*;

import com.twol.sigep.util.Persistencia;


//@RooJpaActiveRecord(finders = { "findDependentesByNomeEquals", "findDependentesByNomeLike" })
@Entity(name= "dependente")
public class Dependente {
	
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
	@Column(nullable = false)
    private String nome;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static void salvar(Dependente d) {
		Persistencia.em.getTransaction().begin();
		Persistencia.em.persist(d);
		Persistencia.em.getTransaction().commit();
	}

	public static void atualizar(Dependente d) {
		Persistencia.em.getTransaction().begin();
		Persistencia.em.merge(d);
		Persistencia.em.getTransaction().commit();
	}

	public static void remover(Dependente d) {
		Persistencia.em.getTransaction().begin();
		Persistencia.em.remove(d);
		Persistencia.em.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public static List<Dependente> recuperarLista() {
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select dependente from Dependente dependente");
		List<Dependente> dependentes = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return dependentes;
	}
}
