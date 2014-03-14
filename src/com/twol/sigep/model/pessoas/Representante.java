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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findRepresentantesByNomeEquals", "findRepresentantesByNomeLike", "findRepresentantesByRazaoSocialDaEmpresaEquals", "findRepresentantesByRazaoSocialDaEmpresaLike" })
@Table(name = "representante")
@Entity
public class Representante extends Entidade{
	
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
    private String razaoSocialDaEmpresa;

    /**
     */
    @Column(nullable = false)
    private String nome;

    /**
     */
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(updatable=true)
	@ForeignKey(name = "endereco_do_representante")
	@OnDelete(action=OnDeleteAction.CASCADE)
    private Endereco endereco;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
	@ForeignKey(name = "telefones_dos_representantes")
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

	public void addTelefone(Telefone telefone) {
		if(this.telefones == null){
			this.telefones = new ArrayList<Telefone>();
		}
		this.telefones.add(telefone);
	}
	
	public void removerTelefone(Telefone telefone) {
		if(this.telefones == null){
			return;
		}
		this.telefones.remove(telefone);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Representante> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select representante from Representante representante");
		List<Representante> representantes = consulta.getResultList();
		return representantes;
    }

	public static Representante recuperarRepresentante(int id) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select r from Representante r where r.id = :idR");
		consulta.setParameter("idR", id);
		Representante representante = (Representante) consulta.getSingleResult();
		return representante;
	}
	
	public static List<Representante> recuperarRepresentantesComNomeQueInicia(String nome) {
		Persistencia.restartConnection();
		
		String stringQuery = "select r FROM Representante as r ";
		stringQuery += "where LOWER(r.nome) LIKE LOWER(:nome) ";
		
		Query consulta = Persistencia.em
				.createQuery(stringQuery);
		consulta.setParameter("nome", nome);
		@SuppressWarnings("unchecked")
		List<Representante> representantes = consulta.getResultList();
		return representantes;
	}
	
	public static List<Representante> recuperarRepresentantesComRazaoSocialDaEmpresaQueInicia(String razaoSocialDaEmpresa) {
		Persistencia.restartConnection();
		
		String stringQuery = "select r FROM Representante as r ";
		stringQuery += "where LOWER(r.razaoSocialDaEmpresa) LIKE LOWER(:razaoSocialDaEmpresa) ";
		
		Query consulta = Persistencia.em
				.createQuery(stringQuery);
		consulta.setParameter("razaoSocialDaEmpresa", razaoSocialDaEmpresa);
		@SuppressWarnings("unchecked")
		List<Representante> representantes = consulta.getResultList();
		return representantes;
	}


    
}
