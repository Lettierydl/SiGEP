package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findClientesByCpfEquals", "findClientesByCpfLike", "findClientesByDataDeNascimentoBetween", "findClientesByNomeEquals", "findClientesByNomeLike" })
@Entity
public class Cliente {
	
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
    @Column(nullable = false, precision = 2)
    private String nome;

    /**
     */
    @OneToOne
    private Endereco endereco;

    /**
     */
    @Column(nullable = false, precision = 2)
    private double debito;

    /**
     */
    @Column(nullable = true,length = 11)
    private String cpf;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataDeNascimento;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Dependente> dependentes = new ArrayList<Dependente>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Telefone> telefones = new ArrayList<Telefone>();
    
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

	public double getDebito() {
		return debito;
	}

	public void setDebito(double debito) {
		this.debito = debito;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Calendar getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(Calendar dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public static void salvar(Cliente c){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(c);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void atualizar(Cliente c){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(c);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Cliente c){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(c);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Cliente> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select cliente from Cliente cliente");
		List<Cliente> clientes = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return clientes;
    }
    
}
