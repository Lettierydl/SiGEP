package com.twol.sigep.model.vendas;
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
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.Persistencia;

@Table(name = "linha_da_venda")
@Entity
public class LinhaDaVenda extends Entidade {
	
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
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(updatable=true)
	@ForeignKey(name = "linha_da_venda")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Venda venda;
	
	
    /**
     */
    @ManyToOne
    private Produto produto;

    /**
     */
    @Column(nullable = false, precision = 4)
    private double quantidadeVendida;

    /**
     */
    @Column(nullable = false, precision = 4)
    private double valorDoDesconto;

    /**
     * Valor que o produto foi vendido quando aconteceu a venda
     */
    @Column(nullable = false, precision = 2)
    private double valorDoProdutoVendido;

    /**
     * Este valor serve apenas para demostracao na GUI
     */
    @Column(nullable = false, precision = 2)
    private double valorTotalDaLinhaSemDesconto;

    /**
     * Este valor serve apenas para demostracao na GUI
     */
    @Column(nullable = false, precision = 2)
    private double valorTotalDaLinhaComDesconto;
    

	public void calcularValores() {
		valorTotalDaLinhaComDesconto = (quantidadeVendida * valorDoProdutoVendido) - valorDoDesconto;
		valorTotalDaLinhaSemDesconto = (quantidadeVendida * valorDoProdutoVendido);
	}
	
	double getValorTotalDaLinhaSemDesconto() {
		return valorTotalDaLinhaSemDesconto;
	}

	double getValorTotalDaLinhaComDesconto() {
		return valorTotalDaLinhaComDesconto;
	}

	@Override
	public String toString(){
		String toString = produto.getDescricao() + " X "+ 
			OperacaoStringUtil.formatarStringQuantidade(quantidadeVendida) + " " +
				produto.getDescricaoUnidade();
		if(valorDoDesconto != 0){
			toString+=" - "+ valorDoDesconto;
		}
		toString+=" = "+ OperacaoStringUtil
				.formatarStringValorMoedaComDescricao(valorTotalDaLinhaComDesconto);
		return toString;
	}
    
	public Venda getVenda() {
		return venda;
	}
	
	/*
		N��o precisa ser setdo pois o addLinhaDaVenda(lv)
		j�� realiza este trabalho
	*/
	void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public double getQuantidadeVendida() {
		return quantidadeVendida;
	}

	public void setQuantidadeVendida(double quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	public double getValorDoDesconto() {
		return valorDoDesconto;
	}

	public void setValorDoDesconto(double valorDoDesconto) {
		this.valorDoDesconto = valorDoDesconto;
	}

	public double getValorDoProdutoVendido() {
		return valorDoProdutoVendido;
	}

	public void setValorDoProdutoVendido(double valorDoProdutoVendido) {
		this.valorDoProdutoVendido = valorDoProdutoVendido;
	}
    
	private static void salvar(LinhaDaVenda e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	private static void atualizar(LinhaDaVenda e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	private static void remover(LinhaDaVenda e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<LinhaDaVenda> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createQuery("select l from LinhaDaVenda l");
		List<LinhaDaVenda> LinhasDaVenda = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return LinhasDaVenda;
    }
	
	@SuppressWarnings("unchecked")
	public static List<LinhaDaVenda> recuperarListaDaVenda(Venda v){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createQuery("select l from LinhaDaVenda as l where l.fk_venda = :idVenda");
		consulta.setParameter("idVenda", v.getId());
		List<LinhaDaVenda> LinhasDaVenda = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return LinhasDaVenda;
    }
}
