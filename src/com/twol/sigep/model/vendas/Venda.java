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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;

//@RooJpaActiveRecord(finders = { "findVendasByCliente", "findVendasByDiaBetween", "findVendasByDiaGreaterThanEquals", "findVendasByFormaDePagamento", "findVendasByFuncionario" })
@Table(name = "venda")
@Entity
public class Venda {
	
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar dia;

    /**
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Funcionario funcionario;

    /**
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    /**
     */
    @ManyToOne(cascade = CascadeType.ALL)
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

    /**
     */
    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venda")
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

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public List<LinhaDaVenda> getLinhasDaVenda() {
		return linhasDaVenda;
	}
	
	public void addLinhaDaVenda(LinhaDaVenda lv){
		if(linhasDaVenda == null){
			this.linhasDaVenda = new ArrayList<LinhaDaVenda>();
		}
		linhasDaVenda.add(lv);
		lv.setVenda(this);
	}
	
	public void removeLinhaDaVenda(LinhaDaVenda lv){
		if(linhasDaVenda == null){
			return;
		}
		linhasDaVenda.remove(lv);
	}
	
	public static void salvar(Venda e){
		if(e.getLinhasDaVenda()!=null && ! e.getLinhasDaVenda().isEmpty()){
			Persistencia.em.getTransaction().begin();
			Persistencia.em.persist(e.getLinhasDaVenda().get(0));
			Persistencia.em.getTransaction().commit();
		}else{
			Persistencia.em.getTransaction().begin();
			Persistencia.em.persist(e);
			Persistencia.em.getTransaction().commit();
		}
    }
	
	public static void atualizar(Venda e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Venda e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Venda> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				
				.createNamedQuery("select venda from Venda venda");
		List<Venda> vendas = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return vendas;
    }
}
