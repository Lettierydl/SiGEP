package com.twol.sigep.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;

public class ControllerPagamento {

	private EntityManagerFactory emf = null;

	public ControllerPagamento(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	/*
	 * Pagamento
	 */
	public void create(Pagamento pagamento) throws ParametrosInvalidosException {
		regrasDeUmPagamento(pagamento);
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(pagamento);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void edit(Pagamento pagamento) throws EntidadeNaoExistenteException,
			Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do Pagamento incluindo
			// listas
			em.getReference(Pagamento.class, pagamento.getId());
			pagamento = em.merge(pagamento);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Pagamento.class, pagamento.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"O Pagamento com código " + pagamento.getId()
								+ " não existe.");
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void destroy(Pagamento pagamento)
			throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				pagamento = em.getReference(Pagamento.class, pagamento.getId());
				pagamento.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"O Pagamento com código " + pagamento.getId()
								+ " não existe.");
			}

			// remove das listas e atualiza entidades relacionadas
			em.remove(pagamento);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void removeAllPagamentos() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM Pagamento WHERE id > 0;",
					Pagamento.class).executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public int getQuantidadePagamentos() {
		EntityManager em = getEntityManager();
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Produto> rt = cq.from(Pagamento.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}
	
	private void regrasDeUmPagamento(Pagamento p) throws ParametrosInvalidosException{
		if(p.getValor() < 0){//pagamentos nao podem ter o valor negativo
			throw new ParametrosInvalidosException("Pagamento com valor negativo");
		}else if(p.getCliente() == null){
			throw new ParametrosInvalidosException("Pagamento sem cliente");
		}
		
	}

}
