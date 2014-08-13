package com.twol.sigep.model.pessoas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.exception.ParametrosInvalidosException;

//@RooJpaActiveRecord(finders = { "findClientesByCpfEquals", "findClientesByCpfLike", "findClientesByDataDeNascimentoBetween", "findClientesByNomeEquals", "findClientesByNomeLike" })
@Table(name = "cliente")
@Entity
public class Cliente {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
	/**
     */
	@Column(nullable = false, precision = 2)
	private String nome;

	/**
     */
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(updatable=true)
	@ForeignKey(name = "endereco_do_cliente")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Endereco endereco;

	/**
	 * Valor total de quanto o cliente deve
     */
	@Column(nullable = false, precision = 2)
	private double debito;

	/**
	 * sem a mascara
     */
	@Column(nullable = true, length = 11)
	private String cpf;

	/**
     */
	@Temporal(TemporalType.DATE)
	private Calendar dataDeNascimento;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="cliente_telefone", joinColumns={@JoinColumn(name="cliente_id")}, inverseJoinColumns={@JoinColumn(name="telefone_id")})
	@ForeignKey(name = "telefones_do_cliente")
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	/**
     
	@OneToMany(cascade = CascadeType.REMOVE , mappedBy = "cliente")
	private List<Dependente> dependentes = new ArrayList<Dependente>();
	
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
	private List<Pagamento> pagamentos = new ArrayList<Pagamento>();
	*/
	 
	
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
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
	
	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void addTelefone(Telefone telefone) {
		if (this.telefones == null) {
			this.telefones = new ArrayList<Telefone>();
		}
		this.telefones.add(telefone);
		//Telefone.salvar(telefone);
	}
	
	public void removerTelefone(Telefone telefone){
		if(this.telefones == null){
			this.telefones = new ArrayList<Telefone>();
			return;
		}
		this.telefones.remove(telefone);
	}
	
	
	/*
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
	
	public List<Pagamento> getPagamentos() {
		return this.pagamentos;
	}
	
	*/

	public void acrecentarDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito += valor;
		} else {
			throw new ParametrosInvalidosException("Não pode ser acencentado um valor negativo ao debito do cliente");
		}
	}

	public void diminuirDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito -= valor;
		} else {
			throw new ParametrosInvalidosException("Não pode ser retirado um valor negativo ao debito do cliente");
		}
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + id;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}	

}
