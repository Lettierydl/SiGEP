package com.twol.sigep.model.vendas;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;

//@RooJpaActiveRecord(finders = { "findVendasByCliente", "findVendasByDiaBetween", "findVendasByDiaGreaterThanEquals", "findVendasByFormaDePagamento", "findVendasByFuncionario" })
@Table(name = "venda")
@Entity
public class Venda implements Comparable<Venda>{
	
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
    
    /**
     */
    @Column(nullable = false, precision = 2)
    private double desconto;

    /**
     * Valor Total da Venda
     */
    @Column(nullable = false, precision = 2)
    private double total;
    
    /**
     * O cliente pode pagar apenas uma parte desta venda
     */
    @Column(nullable = false, precision = 2)
    private double partePaga;
    
    @Column(nullable = false)
    private boolean paga = false;

    /**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "funcionario_da_venda")
    private Funcionario funcionario;
    
    /**
     */
    @OneToMany(orphanRemoval = true, mappedBy = "venda" , cascade={CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH,CascadeType.REFRESH})
    @ForeignKey(name = "itens_da_venda")
	@OnDelete(action=OnDeleteAction.CASCADE)
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemDeVenda> itensDeVenda = new ArrayList<ItemDeVenda>();

    /**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "cliente_da_venda")
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
	
	public double getDesconto() {
		return desconto;
	}

	/**
	 * Valor da venda já com o desconto
     */
	public double getTotalComDesconto() {
		return this.total-this.desconto;
	}

	public double getTotal() {
		return total;
	}
	
	/**
	 * Valor da venda que falta ser pago
     */
	public double getValorNaoPagoDaVenda(){
		return getTotalComDesconto() - partePaga;
	}
	
	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}
	
	public double getPartePagaDaVenda() {
		return partePaga;
	}
	
	public void setPartePagaDaVenda(double partePagaDaVenda) {
		this.partePaga = partePagaDaVenda;
	}

	/**
	 * Acrescenta valor a parte paga da venda
	 * Modifica o estado da venda para pago
	 * @param Valor para ser acrescentado a parte paga da venda
	 */
	public void acrescentarPartePagaDaVenda(double partePagaDaVenda) {
		this.partePaga += partePagaDaVenda;
		if(partePagaDaVenda >= getTotalComDesconto()){
			setPaga(true);
		}
	}
	
	/**
	 * Apenas o ControllerVenda deve utilizar
	 * @param itensDeVenda
	 */
	public void setItensDeVenda(List<ItemDeVenda> itensDeVenda){
		this.itensDeVenda = itensDeVenda;
	}

	public List<ItemDeVenda> getItensDeVenda() {
		if(itensDeVenda == null){
			itensDeVenda = new ArrayList<ItemDeVenda>();
		}
		return itensDeVenda;
	}
	
	static int in = 0;
	int getNewIndex() {
		return in++;
	}
	
	/**
	 * @see Deve ser atualizado a venda logo em seguida para que o item de venda seja salvo
	 * @exception NullPointer se o produto nao existir
	 * @param item já deve vir com produto
	 */
	public void addItemDeVenda(ItemDeVenda item){
		if(item.getProduto()==null){
			throw new NullPointerException("Item de venda sem produto");
		}else{
			this.getItensDeVenda().add(item);
			item.setIndex(getNewIndex());
			item.setVenda(this);
			this.desconto += item.getDesconto();
			this.total += item.getTotal();
		}
	}
	
	/**
	 * @see Este metodo so deve ser utilizado pelo ControllerVenda.removerItemDeVenda(item)
	 * @see Deve ser atualizado a venda e removido o ItemDeVenda do banco logo em seguida
	 * @param item que existe nesta venda
	 */
	public void removeItemDeVenda(ItemDeVenda item){
		if(this.getItensDeVenda().remove(item)){
			//atualizar o index
			this.desconto -= item.getDesconto();
			this.total -= item.getTotal();
			item.setVenda(null);
		}
	}
	
	/**
	 * Ordena da mais antiga para a mais recente
	 */
	@Override
	public int compareTo(Venda o) {
		return o.dia.compareTo(this.dia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(desconto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + (paga ? 1231 : 1237);
		temp = Double.doubleToLongBits(partePaga);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Venda other = (Venda) obj;
		if (Double.doubleToLongBits(desconto) != Double
				.doubleToLongBits(other.desconto))
			return false;
		if (dia == null) {
			if (other.dia != null)
				return false;
		}
		if (id != other.id || (id == 0 || 0 == other.id  ))
			return false;
		if (paga != other.paga)
			return false;
		if (Double.doubleToLongBits(partePaga) != Double
				.doubleToLongBits(other.partePaga))
			return false;
		if (Double.doubleToLongBits(total) != Double
				.doubleToLongBits(other.total))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Venda [id=" + id + ", total=" + total + "]";
	}
	
	
	
}
