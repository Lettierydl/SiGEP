package com.twol.sigep.model.vendas;
import com.twol.sigep.model.estoque.Produto;

import javax.persistence.*;

@Entity(name="linha_da_venda")
public class LinhaDaVenda {
	
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
}
