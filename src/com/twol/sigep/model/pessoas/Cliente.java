package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findClientesByCpfEquals", "findClientesByCpfLike", "findClientesByDataDeNascimentoBetween", "findClientesByNomeEquals", "findClientesByNomeLike" })
@Table(name = "cliente")
@Entity
public class Cliente extends Entidade {
	
	
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
	@Column(nullable = false, precision = 2)
	private String nome;

	/**
     */
	@OneToOne(cascade = CascadeType.PERSIST)
	private Endereco endereco;

	/**
     */
	@Column(nullable = false, precision = 2)
	private double debito;

	/**
     */
	@Column(nullable = true, length = 11)
	private String cpf;

	/**
     */
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataDeNascimento;

	/**
     */
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "cliente")
	private List<Dependente> dependentes = new ArrayList<Dependente>();

	/**
     */
	@OneToMany(cascade = CascadeType.ALL)
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "cliente")
	private List<Pagamento> pagamentos = new ArrayList<Pagamento>();
	 

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Calendar getDataDeNascimento() {
		return dataDeNascimento;
	}
	
	public Date getDataDeNascimentoDate() {
		if(dataDeNascimento != null){
			return dataDeNascimento.getTime();
		}
		return null;
	}

	public void setDataDeNascimento(Calendar dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}
	
	public void setDataDeNascimentoDate(Date dataDeNascimento) {
		this.dataDeNascimento = Calendar.getInstance();
		this.dataDeNascimento.setTime(dataDeNascimento);
	}

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}
	
	public void removerDependente(Dependente dependente){
		if(this.dependentes !=null && !this.dependentes.isEmpty()){
			this.dependentes.remove(dependente);
		}
	}
	
	public void adicionarDependente(Dependente dependente){
		if(this.dependentes !=null){
			this.dependentes.add(dependente);
		}else{
			this.dependentes = new ArrayList<Dependente>();
			adicionarDependente(dependente);
		}
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void addTelefone(Telefone telefone) {
		if (this.telefones == null) {
			this.telefones = new ArrayList<Telefone>();
		}
		this.telefones.add(telefone);
	}
	
	public void removerTelefone(Telefone telefone){
		if(this.telefones == null){
			this.telefones = new ArrayList<Telefone>();
			return;
		}
		this.telefones.remove(telefone);
	}

	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	protected List<?> getListEntidadeRelacionada(){
		return this.getDependentes();
	}

	@SuppressWarnings("unchecked")
	public static List<Cliente> recuperarLista() {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select cliente from Cliente cliente order by nome");
		List<Cliente> clientes = consulta.getResultList();
		return clientes;
	}

	public static Cliente recuperarCliente(int idCliente) {
		Persistencia.restartConnection();
		Query consulta = Persistencia.em.createQuery(
				"select c from Cliente as c where c.id = :idCliente ",
				Cliente.class);
		consulta.setParameter("idCliente", idCliente);
		Cliente c = (Cliente) consulta.getSingleResult();
		return c;
	}

	public void acrecentarDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito += valor;
		} else {
			throw new ParametrosInvalidosException();
		}
	}

	public void diminuirDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito -= valor;
		} else {
			throw new ParametrosInvalidosException();
		}
	}

	public List<Pagamento> getPagamentos() {
		return this.pagamentos;
	}


	

}
