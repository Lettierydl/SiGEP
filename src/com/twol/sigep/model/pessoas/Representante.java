package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findRepresentantesByNomeEquals", "findRepresentantesByNomeLike", "findRepresentantesByRazaoSocialDaEmpresaEquals", "findRepresentantesByRazaoSocialDaEmpresaLike" })
@Entity(name="representante")
public class Representante {
	
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
    private String razaoSocialDaEmpresa;

    /**
     */
    @Column(nullable = false)
    private String nome;

    /**
     */
    @OneToOne
    private Endereco endereco;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Telefone> telefones = new ArrayList<Telefone>();

	public String getRazaoSocialDaEmpresa() {
		return razaoSocialDaEmpresa;
	}

	public void setRazaoSocialDaEmpresa(String razaoSocialDaEmpresa) {
		this.razaoSocialDaEmpresa = razaoSocialDaEmpresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
    
	public static void salvar(Representante e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void atualizar(Representante e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Representante e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Representante> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select representante from Representante representante");
		List<Representante> representantes = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return representantes;
    }
    
}
