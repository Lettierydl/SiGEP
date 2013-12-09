package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

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
    
    @Column(nullable = false, unique = true, length = 50)
    private String login;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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
    
	static void salvar(Funcionario f)  {
			Persistencia.iniciarTrascao();
			try{
				Persistencia.em.persist(f);
			}finally{
				Persistencia.finalizarTrascao();
			}
	}

	static void atualizar(Funcionario e) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(e);
		}finally{
			Persistencia.finalizarTrascao();
		}
	}

	public static void remover(Funcionario e) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove(e);
		}finally{
			Persistencia.finalizarTrascao();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Funcionario> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select funcionario from Funcionario funcionario");
		List<Funcionario> fucionarios = consulta.getResultList();
		return fucionarios;
    }
	
	public static Funcionario recuperarFuncionarioPorLoginESenha
	(String login, String senha){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select f from Funcionario as f where f.login = :lo " +
						"and f.senha = :se");
		consulta.setParameter("lo", login);
		consulta.setParameter("se", senha);
		Funcionario fuc= (Funcionario) consulta.getSingleResult();
		return fuc;
    }
    
    
}
