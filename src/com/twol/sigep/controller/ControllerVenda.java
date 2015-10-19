package com.twol.sigep.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.configuracoes.ConfiguracaoDeVenda;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.ProdutoABaixoDoEstoqueException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.FormaDePagamento;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.SessionUtil;

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

	/*
	 * Divida
	 */
	public void create(Divida divida) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(divida);
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

	public void edit(Divida divida) throws EntidadeNaoExistenteException,
			Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			divida = em.merge(divida);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Divida.class, divida.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("A Divida com código "
						+ divida.getId() + " não existe.");
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void edit(ItemDeVenda item) throws EntidadeNaoExistenteException,
			Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.find(ItemDeVenda.class, item.getId());

			em.getTransaction().begin();
			item = em.merge(item);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(ItemDeVenda.class, item.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"O Item de venda com o código " + item.getId()
								+ " não existe.");
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

	public void destroy(Divida divida) throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				divida = em.getReference(Divida.class, divida.getId());
				divida.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("A Divida com código "
						+ divida.getId() + " não existe.");
			}

			em.remove(divida);
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
				item = em.getReference(ItemDeVenda.class, item.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"O Item de venda com código " + item.getId()
								+ " não existe.");
			}

			try {

				item = em.getReference(ItemDeVenda.class, item.getId());
				em.remove(item);
				em.getTransaction().commit();
			} catch (EntityNotFoundException en) {
			}
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
		List<Pagavel> pagaveis = new ArrayList<Pagavel>();
		pagaveis.addAll(FindVenda.vendasNaoPagaDoCliente(c));
		pagaveis.addAll(FindVenda.dividasNaoPagaDoCliente(c));

		for (Pagavel pag : pagaveis) {
			if (valorRestante == 0) {// ja pagou a venda
				break;
			}
			if (pag.getValorNaoPago() > valorRestante) {// paga parte da
														// venda
				pag.acrescentarPartePaga(valorRestante);
				valorRestante = 0;
			} else {// paga venda toda, pode sobrar resto pra outras vendas ou
					// nao
				valorRestante -= pag.getValorNaoPago();
				pag.acrescentarPartePaga(pag.getValorNaoPago());
			}
			if (pag instanceof Venda) {
				edit((Venda) pag);
			} else if (pag instanceof Divida) {
				edit((Divida) pag);
			}
		}
		// logo em seguida deve diminuir o debito do cliente com o valor do
		// Venda
	}

	/*
	 * Metodos de operações em venda
	 */

	/**
	 * 
	 * @throws VendaPendenteException
	 *             se existir uma venda atual sem ser encerrada
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
	 * 
	 * @exception EntidadeNaoExistenteException
	 *                se a venda atual for vazia
	 */
	public void finalizarVendaAVista(Venda v)
			throws EntidadeNaoExistenteException, Exception {
		v.setFormaDePagamento(FormaDePagamento.A_Vista);
		v.setPaga(true);
		v.setPartePaga(v.getTotal());
		edit(v);
		double valor = retirarItensDoEstoque(v);
		if (valor != v.getTotal()) {
			System.err.println("venda com valor errado");
			// colocar no relatorio do final do dia
		}
	}

	// retorna o que deve ser acrecentado a conta do cliente
	public synchronized double finalizarVendaAPrazo(Venda v, Cliente c,
			double partePaga) throws EntidadeNaoExistenteException, Exception {
		v.setFormaDePagamento(FormaDePagamento.A_Prazo);
		v.setPaga(false);
		v.setPartePaga(partePaga);
		v.setCliente(c);
		edit(v);
		double valor = retirarItensDoEstoque(v);
		if (valor != v.getTotal()) {
			System.err.println("venda com valor errado");
			// colocar no relatorio do final do dia
		}
		double credito = v.getTotal() - v.getPartePaga();
		if (credito < 0) {
			System.err.println("venda com valor errado");
			return 0;
		} else {
			return credito;
		}
	}

	private synchronized double retirarItensDoEstoque(Venda atual)
			throws EntidadeNaoExistenteException, Exception {
		double valor = 0;
		ControllerEstoque ce = new ControllerEstoque(emf);
		for (ItemDeVenda it : atual.getItensDeVenda()) {
			try {
				valor += it.getTotal();
				it.getProduto().removerQuantidadeDeEstoque(it.getQuantidade());
			} catch (ProdutoABaixoDoEstoqueException e) {
				// colocar alguma mensagem no relatorio do final do dia
			} finally {
				ce.edit(it.getProduto());
			}

		}
		return valor;
	}

	public Venda recuperarVendaPendente() throws EntidadeNaoExistenteException,
			Exception {
		List<Venda> pendentes = new FindVenda()
				.getVendasNaoFinalizadasPorFuncionario(logado);
		if (pendentes.size() == 1) {
			atual = pendentes.get(0);
			return pendentes.get(0);
		} else if (pendentes.size() > 1) {
			List<Venda> newPendentes = new ArrayList<Venda>();
			for (Venda p : pendentes) {
				if (null != ConfiguracaoDeVenda.getInstance()
						.getRegistroUsoDeVenda(SessionUtil.obterSession())) {// e
																				// a
																				// venda
																				// da
																				// session
																				// atual
					atual = p;
					atual.setDia(Calendar.getInstance());
					edit(atual);
				} else if (ConfiguracaoDeVenda.getInstance().contensId(
						p.getId())) {// esta no map
					// essa venda ta sendo usada por outro caixa
				} else if (p.getTotal() == 0) {// venda criada sem utilidade
					destroy(p);
				} else {// vendas pendentes que tem valor e nao estavam na
						// sessao de ninquem
					newPendentes.add(p);
				}
			}
			throw new VariasVendasPendentesException(newPendentes);
		} else {
			throw new EntidadeNaoExistenteException();
		}
	}

	public void setAtualComVendaPendenteTemporariamente() {
		Venda melhor_escolha = null;
		List<Venda> pendentes = new FindVenda()
				.getVendasNaoFinalizadasPorFuncionario(logado);
		for (Venda v : pendentes) {
			if (melhor_escolha == null) {
				melhor_escolha = v;
			} else if (melhor_escolha.getTotal() > v.getTotal()) {
				melhor_escolha = v;
			}
		}
		atual = melhor_escolha;
	}

	public void selecionarVendaPendente(int id_venda)
			throws EntidadeNaoExistenteException, Exception {
		List<Venda> pendentes = new FindVenda()
				.getVendasNaoFinalizadasPorFuncionario(logado);
		for (Venda v : pendentes) {
			if (id_venda == v.getId()) {
				atual = v;
				atual.setDia(Calendar.getInstance());
				edit(atual);
				return;
			}
		}
	}

	public void removerVendaPendente(Venda v)
			throws EntidadeNaoExistenteException {
		List<Venda> pendentes = new FindVenda()
				.getVendasNaoFinalizadasPorFuncionario(logado);
		if (pendentes.contains(v)) {
			destroy(v);
		} else {
			System.err.println("Erro linha 340 metodo removerVendaPendente");
		}
	}

	public Venda getAtual() {
		return this.atual;
	}

	public void atualizarDataVendaAtual() throws EntidadeNaoExistenteException,
			Exception {
		this.atual.setDia(Calendar.getInstance());
		this.edit(atual);
	}

	public void addItem(ItemDeVenda it) throws EntidadeNaoExistenteException,
			Exception {
		atual.addItemDeVenda(it);
		this.edit(atual);
	}

	public void removerItem(ItemDeVenda it)
			throws EntidadeNaoExistenteException, Exception {
		
		try{
			refreshAtual();
			it.setVenda(null);
			edit(it);
			try{
				destroy(it);
			}catch(Exception ee){
				// item perdido
			}
		}catch(Exception e){
			throw e;
		}
		refreshAtual();
		atual.removeItemDeVendaJaDeletado(it);
		edit(atual);
		
	}
	
	public void refreshValorVendaAtual() throws EntidadeNaoExistenteException, Exception{
		this.refreshAtual();
		atual.recalcularTotal();
		edit(atual);
		refreshAtual();
	}
	
	private void refreshAtual() {
		EntityManager em = null;
		em = getEntityManager();
		em.getTransaction().begin();
		try {
			atual = em.getReference(Venda.class, atual.getId());
		} catch (EntityNotFoundException enfe) {
		}
		em.getTransaction().commit();

	}

	public int limparBancoDeDados(Date data) {
		int antes = getQuantidadeVendas();
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			Query q = em
					.createNativeQuery(
							"DELETE FROM venda WHERE venda.dia < :data and venda.paga = true;",
							Venda.class);
			q.setParameter("data", data);
			q.executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		int removidas = antes - getQuantidadeVendas();
		return (removidas * 100) / antes;
	}

}
