package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findFuncionariosByCpfEquals", "findFuncionariosByCpfLike", "findFuncionariosByNomeEquals", "findFuncionariosByNomeLike", "findFuncionariosByTipoDeFuncionario" })
@Table(name = "funcionario")
@Entity()
public class Funcionario {
	
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

    /**
     */
    @OneToOne
    private Endereco endereco;

    /**
     */
    @Column(nullable = false)
    private String senha;

    /**
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeFuncionario tipoDeFuncionario;

    /**
     */
    @Column(nullable = false, length = 11)
    private String cpf;
    
    @Column(nullable = false)
    private boolean logado;
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
	
	boolean isLogado() {
		return logado;
	}

	void setLogado(boolean logado) {
		this.logado = logado;
	}

	String getSenha() {
		return senha;
	}

	void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoDeFuncionario getTipoDeFuncionario() {
		return tipoDeFuncionario;
	}

	public void setTipoDeFuncionario(TipoDeFuncionario tipoDeFuncionario) {
		this.tipoDeFuncionario = tipoDeFuncionario;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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
    
	static void salvar(Funcionario e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	static void atualizar(Funcionario e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Funcionario e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Funcionario> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select funcionario from Funcionario funcionario");
		List<Funcionario> fucionarios = consulta.getResultList();
		return fucionarios;
    }
    
    
}
