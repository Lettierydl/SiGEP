package com.twol.sigep.model.vendas;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findVendasByCliente", "findVendasByDiaBetween", "findVendasByDiaGreaterThanEquals", "findVendasByFormaDePagamento", "findVendasByFuncionario" })
@Table(name = "venda")
@Entity
public class Venda extends Entidade implements Comparable<Venda>{
	
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar dia;

    /**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "funcionario_da_venda")
    private Funcionario funcionario;

    /**
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(updatable=true)
    @ForeignKey(name = "cliente_da_venda")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Cliente cliente;

    /**
     */
    @ManyToOne
    @JoinColumn(updatable=true)
    @ForeignKey(name = "dependete_que_fez_a_venda")
    private Dependente dependente;

    /**
     */
    @Column(nullable = false, precision = 2)
    private double valorTotalDeDesconto;

    /**
     */
    @Column(nullable = false, precision = 2)
    private double valorTotalDaVendaComDesconto;

    /**
     */
    @Column(nullable = false, precision = 2)
    private double valorTotalDaVendaSemDesconto;
    
    @Column(nullable = false, precision = 2)
    private double partePagaDaVenda;

    /**
     */
    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;
    
    @Column(nullable = false)
    private boolean paga = false;

    /**
     */
    @OneToMany(orphanRemoval = true, mappedBy = "venda" , fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<LinhaDaVenda> linhasDaVenda = new ArrayList<LinhaDaVenda>();
    
    
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

	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}

	public double getValorTotalDeDesconto() {
		return valorTotalDeDesconto;
	}

	public double getValorTotalDaVendaComDesconto() {
		return valorTotalDaVendaComDesconto;
	}

	public double getValorTotalDaVendaSemDesconto() {
		return valorTotalDaVendaSemDesconto;
	}
	
	//restante da venda que não foi pago ainda
	public double getValorNaoPagoDaVenda(){
		return valorTotalDaVendaComDesconto - partePagaDaVenda;
	}
	
	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}
	
	public double getPartePagaDaVenda() {
		return partePagaDaVenda;
	}
	
	public void setPartePagaDaVenda(double partePagaDaVenda) {
		this.partePagaDaVenda = partePagaDaVenda;
	}

	public void acrescentarPartePagaDaVenda(double partePagaDaVenda) {
		this.partePagaDaVenda += partePagaDaVenda;
		if(partePagaDaVenda >= valorTotalDaVendaComDesconto){
			setPaga(true);
		}
	}
	
	public int getQuantidadeDeItens(){
		int resu = 0;
		for(LinhaDaVenda lv : getLinhasDaVenda()){
			if(Double.valueOf(lv.getQuantidadeVendida()).floatValue() == 0){
				resu += lv.getQuantidadeVendida();
			}else{// so conta um unidade pra 6,8 kg de alguma coisa por exempro
				resu++;
			}
		}
		return resu;
	}

	public List<LinhaDaVenda> getLinhasDaVenda() {
		return linhasDaVenda;
	}
	
	/*
	 * Atualiza altomaticamente os valores totais da venda
	 */
	void addLinhaDaVenda(LinhaDaVenda lv){
		if(linhasDaVenda == null){
			this.linhasDaVenda = new ArrayList<LinhaDaVenda>();
		}
		linhasDaVenda.add(lv);
		lv.setVenda(this);
		valorTotalDaVendaComDesconto +=lv.getValorTotalDaLinhaComDesconto();
		valorTotalDaVendaSemDesconto +=lv.getValorTotalDaLinhaSemDesconto();
		valorTotalDeDesconto +=lv.getValorDoDesconto();
		LinhaDaVenda.salvar(lv);
	}
	
	void removeLinhaDaVenda(LinhaDaVenda lv){
		if(linhasDaVenda == null){
			return;
		}
		linhasDaVenda.remove(lv);
		valorTotalDaVendaComDesconto -=lv.getValorTotalDaLinhaComDesconto();
		valorTotalDaVendaSemDesconto -=lv.getValorTotalDaLinhaSemDesconto();
		valorTotalDeDesconto -=lv.getValorDoDesconto();
		LinhaDaVenda.remover(lv);
	}
	
	protected List<?> getListEntidadeRelacionada(){
		return this.getLinhasDaVenda();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Venda> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				
				.createQuery("select venda from Venda venda");
		List<Venda> vendas = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return vendas;
    }

	@Override//ordena do menor para o maior (valor da venda)
	public int compareTo(Venda o) {
		if(o.getValorTotalDaVendaComDesconto()==getValorTotalDaVendaComDesconto()){
			return 0;
		}
		return o.getValorTotalDaVendaComDesconto() < getValorTotalDaVendaComDesconto() ? 1 : -1;
	}
}
