package com.twol.sigep.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.vendas.Pagavel;

public class ControllerPessoa extends Controller {

	/*
	 * Cliente
	 */
	public void create(Cliente cliente) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(cliente);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void edit(Cliente cliente) throws EntidadeNaoExistenteException,
			Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do produto incluindo listas
			cliente = em.merge(cliente);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Cliente.class, cliente.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("O cliente "
						+ cliente.getNome() + " não existe.");
			}
			throw ex;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void destroy(Cliente cliente) throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				cliente = em.getReference(Cliente.class, cliente.getId());
				cliente.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("O cliente "
						+ cliente.getNome() + " não existe.");
			}
			// remove das listas e atualiza entidades relacionadas
			em.remove(cliente);
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/*
	 * Dependente
	 */
	public void create(Dependente dependente) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(dependente);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void edit(Dependente dependente)
			throws EntidadeNaoExistenteException, Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do produto incluindo listas
			dependente = em.merge(dependente);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Dependente.class, dependente.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("O dependente "
						+ dependente.getNome() + " não existe.");
			}
			throw ex;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void destroy(Dependente dependente)
			throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				dependente = em.getReference(Dependente.class,
						dependente.getId());
				dependente.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("O cliente "
						+ dependente.getNome() + " não existe.");
			}
			// remove das listas e atualiza entidades relacionadas
			em.remove(dependente);
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/*
	 * Funcionario
	 */
	public void create(Funcionario funcionario) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(funcionario);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void edit(Funcionario funcionario)
			throws EntidadeNaoExistenteException, Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do produto incluindo listas
			funcionario = em.merge(funcionario);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Funcionario.class, funcionario.getId());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("O funcionário "
						+ funcionario.getNome() + " não existe.");
			}
			throw ex;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void destroy(Funcionario funcionario)
			throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				funcionario = em.find(Funcionario.class, funcionario.getId());
				funcionario.getId();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException("A funcion��rio "
						+ funcionario.getNome() + " n��o existe.");
			}

			// remove das listas e atualiza entidades relacionadas

			em.remove(funcionario);
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	// metodos para testes
	public void removeAllClientes() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM Cliente WHERE id > 0;",
					Cliente.class).executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public void removeAllFuncionarios() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM Funcionario WHERE id > 0;",
					Cliente.class).executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int getQuantidadeClientes() {
		EntityManager em = getEntityManager();
		try {
			@SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Produto> rt = cq.from(Cliente.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int getQuantidadeFuncionarios() {
		EntityManager em = getEntityManager();
		try {
			@SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Produto> rt = cq.from(Funcionario.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public double recalcularDebitoDoCliente(Cliente c) throws EntidadeNaoExistenteException, Exception {
		List<Pagavel> di_ve = new FindVenda().pagavelNaoPagoDoCliente(c);
		double deb_real = 0;
		for (Pagavel p : di_ve) {
			deb_real += p.getValorNaoPago();
		}

		c = getEntityManager().find(Cliente.class, c.getId());
		try {
			if (deb_real != c.getDebito()) {
				if (deb_real - c.getDebito() > 0.0) {
					c.acrecentarDebito( new BigDecimal( deb_real - c.getDebito()).setScale(2,
							RoundingMode.HALF_UP).doubleValue());
				} else {
					c.diminuirDebito(new BigDecimal(c.getDebito() - deb_real).setScale(2,
									RoundingMode.HALF_UP).doubleValue()
									);
				}
				edit(c);
			}
		} catch (ParametrosInvalidosException e) {
			e.printStackTrace();
			throw e;
		}
		return c.getDebito();
	}

}
