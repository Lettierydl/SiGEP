package com.twol.sigep.model.vendas;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;

@Table(name = "divida")
@Entity
public class Divida implements Comparable<Divida> , Pagavel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
    /**
     * Dia e hora que aconteceu a venda
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar dia;

    
    @Column()
    private String descricao;
    
    /**
     * Valor Total da Venda
     */
    @Column(nullable = false, precision = 2)
    private double total;
    
    /**
     * O cliente pode pagar apenas uma parte desta divida
     */
    @Column(nullable = false, precision = 2)
    private double partePaga;
    
    @Column(nullable = false)
    private boolean paga = false;

    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;


	/**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "funcionario_da_divida")
    private Funcionario funcionario;
    
  

    /**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "cliente_da_divida")
    @OnDelete(action=OnDeleteAction.NO_ACTION)
    private Cliente cliente;
    
    public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
	
    public Calendar getDia() {
		return dia;
	}

	public void setDia(Calendar dia) {
		this.dia = dia;
	}
	
    public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorNaoPago(){
		return getTotal() - partePaga;
	}

	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total){
		this.total = total;
	}
	
	
	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}
	
	public double getPartePaga() {
		return partePaga;
	}
	
	public void setPartePaga(double partePagaDaDivida) {
		this.partePaga = partePagaDaDivida;
	}

	/**
	 * Acrescenta valor a parte paga da venda
	 * Modifica o estado da divida para pago
	 * @param Valor para ser acrescentado a parte paga da divida
	 */
	public void acrescentarPartePaga(double partePagaDaDivida) {
		this.partePaga += partePagaDaDivida;
		this.partePaga = new BigDecimal(partePaga).setScale(5, RoundingMode.HALF_UP).doubleValue();
		if(this.partePaga >= getTotal()){
			setPaga(true);
		}
	}
	
	
	/**
	 * Ordena da mais antiga para a mais recente
	 */
	@Override
	public int compareTo(Divida o) {
		return o.dia.compareTo(this.dia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime
				* result
				+ ((formaDePagamento == null) ? 0 : formaDePagamento.hashCode());
		result = prime * result + id;
		long temp;
		temp = Double.doubleToLongBits(total);
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
		Divida other = (Divida) obj;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		if (formaDePagamento != other.formaDePagamento)
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(total) != Double
				.doubleToLongBits(other.total))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Divida [id=" + id + ", total=" + total + "]";
	}
	
	
	
}
