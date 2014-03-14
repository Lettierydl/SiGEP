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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(updatable=true)
	@ForeignKey(name = "endereco_do_funcionario")
	@OnDelete(action=OnDeleteAction.CASCADE)
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
    
    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    @ForeignKey(name = "telefones_dos_funcionarios")
    private List<Telefone> telefones = new ArrayList<Telefone>();
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "funcionario")
    private List<Pagamento> pagamentos = new ArrayList<Pagamento>();

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

	String getSenha() {
		return senha;
	}

	void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoDeFuncionario getTipoDeFuncionario() {
		return tipoDeFuncionario;
	}

	void setTipoDeFuncionario(TipoDeFuncionario tipoDeFuncionario) {
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
	
	public List<Pagamento> getPagamentos(){
		return this.pagamentos;
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
    
	static void salvar(Funcionario f)  {
			Persistencia.iniciarTrascao();
			try{
				Persistencia.em.persist(f);
			}catch(Exception e){
				e.printStackTrace();
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
			Persistencia.em.remove(Persistencia.em.getReference(e.getClass(), e.getId()));
		}catch(Exception j){
			j.printStackTrace();
		}finally{
			Persistencia.finalizarTrascao();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Funcionario> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select funcionario from Funcionario funcionario order by nome");
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

	@SuppressWarnings("unchecked")
	public static List<Funcionario> recuperarFuncionarioPorNomeQueInicia(String nome) {
		Persistencia.restartConnection();
		nome = nome +"%";
		Query q = Persistencia.em
				.createQuery(
						"select f from Funcionario as f where LOWER(f.nome) LIKE LOWER(:nome) order by nome",
						Funcionario.class);
		q.setParameter("nome", nome);
		List<Funcionario> fucionarios = q.getResultList();
		return fucionarios;
	}
    
    
}
