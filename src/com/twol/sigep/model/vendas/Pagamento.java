package com.twol.sigep.model.vendas;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

@Table(name = "pagamento")
@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	
	@Column(nullable = false, precision = 2)
	private double valor;
	
	@Column
	private String observacao;
	
	 /**
	  * Como foi efetuado o Pagamento
     */
    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;


	@ManyToOne
	@JoinColumn(updatable=true)
	@ForeignKey(name = "pagamentos_do_cliente")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(updatable = true)
	@ForeignKey(name = "funcionario_que_registrou_pagamento")
	private Funcionario funcionario;
	
	public Pagamento(){
		this.data = Calendar.getInstance();
	}
	
	

	public Pagamento( double valor, Cliente cliente, Funcionario funcionario) {
		super();
		this.data = Calendar.getInstance();
		this.valor = valor;
		this.cliente = cliente;
		this.funcionario = funcionario;
	}



	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}



	
	
	public static void salvar(Pagamento p)  {
			Persistencia.iniciarTrascao();
			try{
				Persistencia.em.persist(p);
			}finally{
				Persistencia.finalizarTrascao();
				Persistencia.flush();
			}
	}
	
	public static void atualizar(Pagamento p) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(p);
		}finally{
			Persistencia.finalizarTrascao();
		}
	}
	
	public static void remover(Pagamento e) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove(e);
		}finally{
			Persistencia.finalizarTrascao();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Pagamento> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select pagamento from Pagamento pagamento order by data");
		List<Pagamento> pagamentos = consulta.getResultList();
		return pagamentos;
	}
	
	public static Funcionario recuperarPagamentoPorFuncionario(Funcionario f){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select p from Pagamento as p where p.funcionario_id = :func_id ");
		consulta.setParameter("func_id", f.getId());
		Funcionario fuc= (Funcionario) consulta.getSingleResult();
		return fuc;
	}
	
	public static Pagamento recuperarPagamentoPorID(int id){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select p from Pagamento as p where p.id = :id ");
		consulta.setParameter("id", id);
		Pagamento pag= (Pagamento) consulta.getSingleResult();
		return pag;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((observacao == null) ? 0 : observacao.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Pagamento other = (Pagamento) obj;
		if (id != other.id || (id == 0 || 0 == other.id  ) )
			return false;
		if (observacao == null) {
			if (other.observacao != null)
				return false;
		} else if (!observacao.equals(other.observacao))
			return false;
		if (Double.doubleToLongBits(valor) != Double
				.doubleToLongBits(other.valor))
			return false;
		return true;
	}

	
	
	
}
