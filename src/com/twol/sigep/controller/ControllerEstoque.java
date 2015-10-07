package com.twol.sigep.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.Promocao;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;

public class ControllerEstoque {

	private EntityManagerFactory emf = null;
	
	public ControllerEstoque(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	private EntityManager getEntityManager(){
		return emf.createEntityManager();
	}
	
	
	/*
	 * Produto
	 */
	public void create(Produto produto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(produto);
            //realizar testes de dependencias e dar refreshe em objetos embutidos
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws EntidadeNaoExistenteException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //atualiza entidades de relacionamento do produto incluindo listas
            em.getReference(Produto.class, produto.getId());
            produto = em.merge(produto);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Produto.class, produto.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("O produto com código de barras " + produto.getCodigoDeBarras() + " não existe.");
        	}
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Produto produto) throws EntidadeNaoExistenteException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                produto = em.getReference(Produto.class, produto.getId());
                produto.getId();
            } catch (EntityNotFoundException enfe) {
            	throw new EntidadeNaoExistenteException("O produto com código de barras " + produto.getCodigoDeBarras() + " não existe.");
            }
            //remove das listas e atualiza entidades relacionadas
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }    
    
    
    
    /*
	 * Promocoes
	 */
	public void create(Promocao promocao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(promocao);
            //realizar testes de dependencias e dar refreshe em objetos embutidos
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Promocao promocao) throws EntidadeNaoExistenteException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //atualiza entidades de relacionamento do produto incluindo listas
            promocao = em.merge(promocao);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Promocao.class, promocao.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("A promoção com código " + promocao.getId() + " não existe.");
        	}
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Promocao promocao) throws EntidadeNaoExistenteException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
            	promocao = em.find(Promocao.class, promocao.getId());
            	promocao.getId();
            } catch (EntityNotFoundException enfe) {
            	throw new EntidadeNaoExistenteException("A promoção com código " + promocao.getId() + " não existe.");
            }
            
            //remove das listas e atualiza entidades relacionadas 
            Produto prod = promocao.getProduto();
            if(prod!=null){
            	//prod = em.find(Produto.class, prod.getId());
            	prod.removerPromocao(promocao);
            }
            em.remove(promocao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }    
    
    
    
    
    
    
    
  //metodos para testes
    public void removeAllProdutos(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM Produto WHERE id > 0;", Produto.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public int getQuantidadeProdutos() {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public void removeAllPromocoes(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM Promocao WHERE id > 0;", Promocao.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public int getQuantidadePromocoes() {
        EntityManager em = getEntityManager();
        try {
            @SuppressWarnings("rawtypes")
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Promocao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
	
	
}
