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
import javax.persistence.Table;

import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findRepresentantesByNomeEquals", "findRepresentantesByNomeLike", "findRepresentantesByRazaoSocialDaEmpresaEquals", "findRepresentantesByRazaoSocialDaEmpresaLike" })
@Table(name = "representante")
@Entity
public class Representante extends Pessoa{
	
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

	public void addTelefones(Telefone telefone) {
		if(this.telefones == null){
			this.telefones = new ArrayList<Telefone>();
		}
		this.telefones.add(telefone);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Representante> recuperarLista(){
		Query consulta = Persistencia.em
				.createNamedQuery("select representante from Representante representante");
		List<Representante> representantes = consulta.getResultList();
		return representantes;
    }
    
}
