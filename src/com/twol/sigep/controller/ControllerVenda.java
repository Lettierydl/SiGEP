package com.twol.sigep.controller;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;

public class ControllerVenda {

	private EntityManagerFactory emf = null;
	private Venda atual;
	private Funcionario logado;
	
	public ControllerVenda(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	/*
	 * Venda
	 */
	public void create(Venda Venda) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(Venda);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void edit(Venda venda) throws EntidadeNaoExistenteException,
			Exception {
		EntityManager em = null;
		try {
			for (ItemDeVenda it : venda.getItensDeVenda()) {
				if (it.getId() == 0) {
					create(it);
				}
			}
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do Venda incluindo
			// listas

			//
			venda = em.merge(venda);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Venda.class, venda.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("A Venda com código "
						+ venda.getId() + " não existe.");
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void destroy(Venda Venda) throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				Venda = em.getReference(Venda.class, Venda.getId());
				Venda.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("A Venda com código "
						+ Venda.getId() + " não existe.");
			}

			// remove das listas e atualiza entidades relacionadas
			em.remove(Venda);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void destroy(ItemDeVenda item) throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				em.getReference(Venda.class, item.getVenda().getId());
				item = em.getReference(ItemDeVenda.class, item.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"O Item de venda com código " + item.getId()
								+ " não existe.");
			}
			
			item.setVenda(null);
			em.merge(item);
			em.getTransaction().commit();
			
			em = getEntityManager();
			em.getTransaction().begin();
			item = em.getReference(ItemDeVenda.class, item.getId());
			em.remove(item);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void create(ItemDeVenda item) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(item);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void removeAllVendas() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM Venda WHERE id > 0;", Venda.class)
					.executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getQuantidadeVendas() {
		EntityManager em = getEntityManager();
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Produto> rt = cq.from(Venda.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}

	/*
	 * Regra de negocio da venda
	 */
	public void setLogado(Funcionario logado) {
		this.logado = logado;
	}
	
	/**
	 * Verifica quais vendas podem ser pagas<br/>
	 * Se não puder pagar uma venda completa é acrescentado uma parte paga na
	 * venda
	 * 
	 * @see ControllerPessoa.edit(Cliente c) deve ser chamado logo em seguida
	 *      para atualizar o debito do cliente
	 * @param Pagamento
	 *            que foi efetuado
	 * @throws Exception
	 * @throws EntidadeNaoExistenteException
	 *             se venda não existir
	 */
	public void abaterValorDoPagamentoNaVenda(Pagamento p)
			throws EntidadeNaoExistenteException, Exception {
		Cliente c = p.getCliente();
		double valorRestante = p.getValor();
		for (Venda v : FindVenda.vendasNaoPagaDoCliente(c)) {
			if (valorRestante == 0) {// ja pagou a venda
				break;
			}
			if (v.getValorNaoPagoDaVenda() > valorRestante) {// paga parte da
																// venda
				v.acrescentarPartePagaDaVenda(valorRestante);
				valorRestante = 0;
			} else {// paga venda toda, pode sobrar resto pra outras vendas ou
					// nao
				valorRestante -= v.getValorNaoPagoDaVenda();
				v.acrescentarPartePagaDaVenda(v.getValorNaoPagoDaVenda());
			}
			edit(v);
		}
		// logo em seguida deve diminuir o debito do cliente com o valor do
		// Venda
	}

	/*
	 * Metodos de operações em venda
	 */
	
	/**
	 * 
	 * @throws VendaPendenteException se existir uma venda atual sem ser encerrada
	 */
	public void iniciarNovaVenda() throws VendaPendenteException {
		if (atual == null) {
			atual = new Venda();
			atual.setFuncionario(logado);
			atual.setDia(Calendar.getInstance());
			create(atual);
		} else {
			throw new VendaPendenteException();
		}
	}
	
	/**
	 * Inicia uma nova venda vazia
	 * @exception EntidadeNaoExistenteException se a venda atual for vazia
	 */
	public double finalizarVendaAVista(double valorPago) throws EntidadeNaoExistenteException, Exception{
		edit(atual);
		double troco = valorPago - atual.getTotalComDesconto();
		atual = null;
		return troco;
	}
	
	
	/**
	 * @retur Retorna o valor que deve acrecentar ao debito do cliente
	 * @see ControllerPessoa.edit(Cliente c) deve ser chamado logo em seguida
	 *      para atualizar o debito do cliente.
	 */
	public double finalizarVendaAPrazo(Cliente cliente) throws EntidadeNaoExistenteException, Exception{
		atual.setCliente(cliente);
		edit(atual);
		double debito = atual.getTotalComDesconto();
		atual = null;
		return debito;
	}
	
	public Venda recuperarVendaPendente() throws VariasVendasPendentesException, EntidadeNaoExistenteException {
		List<Venda> pendentes = new FindVenda()
				.getVendasNaoFinalizadasPorFuncionario(logado);
		if(pendentes.size()==1){
			atual = pendentes.get(0);
			return pendentes.get(0);
		}else if(pendentes.size() > 1){
			throw new VariasVendasPendentesException(pendentes);
		}else{
			throw new EntidadeNaoExistenteException();
		}
	}

	public Venda getAtual() {
		return this.atual;
	}

	public void addItem(ItemDeVenda it) throws EntidadeNaoExistenteException, Exception {
		atual.addItemDeVenda(it);
		this.edit(atual);
	}
	
	public void removerItem(ItemDeVenda it) throws EntidadeNaoExistenteException, Exception {
		atual.removeItemDeVenda(it);
		destroy(it);
		edit(atual);
	}
	
	

}
