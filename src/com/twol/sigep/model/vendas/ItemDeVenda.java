package com.twol.sigep.model.vendas;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.util.OperacaoStringUtil;

@Table(name = "item_de_venda")
@Entity
public class ItemDeVenda  implements Comparable<ItemDeVenda>{

	public ItemDeVenda(){}
	
	public ItemDeVenda(Produto p, double quantidade){
		this.setQuantidade(quantidade);
		this.setProduto(p);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
	/**
	 * Indice do item na venda
	 */
	@Column(nullable = false)
	private int indice;
	
	/**
	 * Quantidade do vendida do produto
	 */
	@Column(nullable = false, precision=4)
	private double quantidade;
	
	

	/**
	 * Valor que o produto foi vendido quando aconteceu a venda
	 */
	@Column(nullable = false, precision = 2)
	private double valorProduto;

	/**
	 * Total da venda Sem contabilizar o desconto da promoção
	 */
	@Column(nullable = false, precision = 2)
	private double total;

	/**
	 * Valor de desconto do item se tiver promoção valida
	 */
	@Column(nullable = false, precision = 4)
	private double desconto;

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.DETACH })
	@JoinColumn(updatable = true)
	@ForeignKey(name = "item_da_venda")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private Venda venda;

	/**
     */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = true)
	@ForeignKey(name = "produto_vendido")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Produto produto;

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
	
	public int getIndex() {
		return indice;
	}
	
	void setIndex(int index){
		this.indice = index;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
		if(this.produto!=null){
			setValores(this.produto);
		}
	}

	public double getValorProduto() {
		return valorProduto;
	}

	public double getTotal() {
		return total;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		setValores(this.produto);
	}

	public double getTotalComDesconto() {
		return total - desconto;
	}

	@Override
	public String toString() {
		String toString = getId()+" "+getIndex() + " "+produto.getDescricao() + " X "
				+ OperacaoStringUtil.formatarStringQuantidade(quantidade) + " "
				+ produto.getDescricaoUnidade();
		
		/*if (desconto != 0) {
			toString += " - " + desconto;
		}*/
		toString += " = "
				+ OperacaoStringUtil
						.formatarStringValorMoedaComDescricao(getTotalComDesconto());
		return toString;
	}

	// deve ser utilizado apenas quando for setar o produto pela primeira vez
	private void setValores(Produto p) {
		valorProduto = p.getValorDeVenda();
		total = (quantidade * valorProduto);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		ItemDeVenda other = (ItemDeVenda) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(ItemDeVenda o) {
		return Integer.compare(o.id, id);
	}
	
	
}
