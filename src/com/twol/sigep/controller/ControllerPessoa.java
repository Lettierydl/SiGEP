package com.twol.sigep.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;

public class ControllerPessoa {

	private EntityManagerFactory emf = null;
	
	public ControllerPessoa(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	private EntityManager getEntityManager(){
		return emf.createEntityManager();
	}
	
	
	/*
	 * Cliente
	 */
	public void create(Cliente cliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cliente);
            //realizar testes de dependencias e dar refreshe em objetos embutidos
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws EntidadeNaoExistenteException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //atualiza entidades de relacionamento do produto incluindo listas
            cliente = em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Cliente.class, cliente.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("O cliente " + cliente.getNome() + " n��o existe.");
        	}
            throw ex;
        } finally {
            if (em != null) {
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
            	throw new EntidadeNaoExistenteException("O cliente " + cliente.getNome() + " n��o existe.");
            }
            //remove das listas e atualiza entidades relacionadas
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
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
            //realizar testes de dependencias e dar refreshe em objetos embutidos
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws EntidadeNaoExistenteException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //atualiza entidades de relacionamento do produto incluindo listas
            funcionario = em.merge(funcionario);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Funcionario.class, funcionario.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("O funcion��rio " + funcionario.getNome() + " n��o existe.");
        	}
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Funcionario funcionario) throws EntidadeNaoExistenteException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
            	funcionario = em.find(Funcionario.class, funcionario.getId());
            	funcionario.getId();
            } catch (EntityNotFoundException enfe) {
            	throw new EntidadeNaoExistenteException("A funcion��rio " + funcionario.getNome() + " n��o existe.");
            }
            
            //remove das listas e atualiza entidades relacionadas 
          
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }    
   
    
    
    
    
    
    
    
    
    
    
    
    
  //metodos para testes
    public void removeAllClientes(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM Cliente WHERE id > 0;", Cliente.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void removeAllFuncionarios(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM Funcionario WHERE id > 0;", Cliente.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
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
            em.close();
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
            em.close();
        }
    }
   
   
	
	
}
