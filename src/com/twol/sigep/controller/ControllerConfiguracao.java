package com.twol.sigep.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.model.configuracoes.Configuracao;
import com.twol.sigep.model.configuracoes.ConfiguracaoPK;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.util.Persistencia;

public class ControllerConfiguracao {

	private static EntityManagerFactory emf = null;

	public ControllerConfiguracao(EntityManagerFactory emf) {
		ControllerConfiguracao.setEmf(emf);
	}

	private static EntityManager getEntityManager() {
		return getEmf().createEntityManager();
	}

	/*
	 * Configuracao
	 */
	private static void create(Configuracao config) {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.persist(config);
			// realizar testes de dependencias e dar refreshe em objetos
			// embutidos
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private static void edit(Configuracao config)
			throws EntidadeNaoExistenteException, Exception {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			// atualiza entidades de relacionamento do Pagamento incluindo
			// listas
			
			ConfiguracaoPK cpk = new ConfiguracaoPK();
			cpk.setChave(config.getChave());
			cpk.setTipoDeFuncionario(config.getTipoDeFuncionario());
			
			em.getReference(Configuracao.class, cpk);
			config = em.merge(config);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				em.find(Configuracao.class, config.getChave());
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"A Configuração com código " + config.getChave()
								+ " não existe.");
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public static void destroy(Configuracao config)
			throws EntidadeNaoExistenteException {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			try {
				config = em.getReference(Configuracao.class, config.getChave());
				config.getChave();
			} catch (EntityNotFoundException enfe) {
				throw new EntidadeNaoExistenteException(
						"A Configuracão com a chave " + config.getChave()
								+ " não existe.");
			}

			// remove das listas e atualiza entidades relacionadas
			em.remove(config);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public static void removeAllConfiguracoes() {
		EntityManager em = null;
		try {
			em = getEntityManager();
			em.getTransaction().begin();
			em.createNativeQuery("DELETE FROM Configuracao;", Pagamento.class)
					.executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static int getQuantidadeConfiguracoes() {
		EntityManager em = getEntityManager();
		try {
			@SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<Produto> rt = cq.from(Configuracao.class);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}

	public static boolean getValor(String chave, TipoDeFuncionario tipo)
			throws EntityNotFoundException {
		EntityManager em = getEntityManager();
		try {
			String stringQuery = "select c FROM Configuracao as c ";
			stringQuery += "where c.chave = :chave and " +
								 "c.tipoDeFuncionario = :tipo ";
			
			Persistencia.restartConnection();
			Query query = Persistencia.em.createQuery(stringQuery, Configuracao.class);
			
			query.setParameter("tipo", tipo);
			query.setParameter("chave", chave);
			
			Configuracao con = (Configuracao) query.getSingleResult();
			return con.isValor();
		} catch (EntityNotFoundException ee) {
			Configuracao c = new Configuracao(chave, false, tipo);
			create(c);
			return false;
		}catch (NoResultException nr) {
			Configuracao c = new Configuracao(chave, false, tipo);
			create(c);
			return false;
		}
		finally {
			em.close();
		}
	}

	public static void putValor(String chave, boolean valor,
			TipoDeFuncionario tipo) {
		EntityManager em = getEntityManager();
		try {

			ConfiguracaoPK cpk = new ConfiguracaoPK();
			cpk.setChave(chave);
			cpk.setTipoDeFuncionario(tipo);

			Configuracao con = em.getReference(Configuracao.class, cpk);
			con.setValor(valor);
			edit(con);
		} catch (Exception e) {
			Configuracao c = new Configuracao(chave, valor, tipo);
			create(c);
		} finally {
			em.close();
		}
	}

	public static EntityManagerFactory getEmf() {
		return emf;
	}

	public static void setEmf(EntityManagerFactory emf) {
		ControllerConfiguracao.emf = emf;
	}

}