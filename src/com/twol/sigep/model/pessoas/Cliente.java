package com.twol.sigep.model.pessoas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.util.OperacaoStringUtil;

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
	@Column(nullable = false, precision = 2, unique = true)
	private String nome;

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
	
	@Column(nullable = true, length = 15)
	private String telefone;
	
	@Column(nullable = true, length = 15)
	private String celular;
	
	@Column(nullable = true, length = 500)
	private String endereco;
	
	
	@OneToMany(cascade = CascadeType.REMOVE , mappedBy = "cliente")
	private List<Dependente> dependentes = new ArrayList<Dependente>();
	

	/**
     
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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return OperacaoStringUtil.formatarStringParaMascaraDeTelefone(telefone);
	}

	public void setTelefone(String telefone) {
		this.telefone = OperacaoStringUtil.retirarMascaraDeTelefone(telefone);
	}

	public String getCelular() {
		return OperacaoStringUtil.formatarStringParaMascaraDeTelefone(celular);
	}

	public void setCelular(String celular) {
		this.celular = OperacaoStringUtil.retirarMascaraDeTelefone(celular);
	}

	public double getDebito() {
		debito = new BigDecimal(this.debito).setScale(2,
				RoundingMode.HALF_UP).doubleValue();
		return debito;
	}

	public String getCpf() {
		return OperacaoStringUtil.formatarStringParaMascaraDeCPF(cpf);
	}

	public void setCpf(String cpf) {
		this.cpf = OperacaoStringUtil.retirarMascaraDeCPF(cpf);
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
		if(dataDeNascimento != null){
			this.dataDeNascimento = Calendar.getInstance();
			this.dataDeNascimento.setTime(dataDeNascimento);
		}
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
	/*
	public List<Pagamento> getPagamentos() {
		return this.pagamentos;
	}
	
	*/

	public void acrecentarDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito += valor;
			debito = new BigDecimal(this.debito).setScale(2,
					RoundingMode.HALF_UP).doubleValue();
		} else {
			throw new ParametrosInvalidosException("Não pode ser acencentado um valor negativo ao debito do cliente");
		}
	}

	public void diminuirDebito(double valor)
			throws ParametrosInvalidosException {
		if (valor > 0) {
			debito -= valor;
			debito = new BigDecimal(this.debito).setScale(2,
					RoundingMode.HALF_UP).doubleValue();
		} else {
			throw new ParametrosInvalidosException("N��o pode ser retirado um valor negativo ao debito do cliente");
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
