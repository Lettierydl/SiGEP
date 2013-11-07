package com.twol.sigep.model.vendas;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;

//@RooJpaActiveRecord(finders = { "findVendasByCliente", "findVendasByDiaBetween", "findVendasByDiaGreaterThanEquals", "findVendasByFormaDePagamento", "findVendasByFuncionario" })
@Entity(name="venda")
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
    @ManyToOne
    private Funcionario funcionario;

    /**
     */
    @ManyToOne
    private Cliente cliente;

    /**
     */
    @ManyToOne
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<LinhaDaVenda> linhasDaVenda = new ArrayList<LinhaDaVenda>();
}
