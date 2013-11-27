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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findClientesByCpfEquals", "findClientesByCpfLike", "findClientesByDataDeNascimentoBetween", "findClientesByNomeEquals", "findClientesByNomeLike" })
@Table(name = "cliente")
@Entity
public class Cliente extends Pessoa {


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
	@OneToOne(cascade = CascadeType.ALL)
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
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
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

	public void addTelefones(Telefone telefone) {
		if (this.telefones == null) {
			this.telefones = new ArrayList<Telefone>();
		}
		this.telefones.add(telefone);
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
		Query consulta = Persistencia.em
				.createQuery("select cliente from Cliente cliente");
		List<Cliente> clientes = consulta.getResultList();
		return clientes;
	}

	public static Cliente recuperarCliente(int idCliente) {
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

}
