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
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Telefone;

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
            Cliente clienteold = em.getReference(Cliente.class, cliente.getId());
            if(clienteold.getEndereco()!=null && cliente.getEndereco()==null){
            	em.remove(clienteold.getEndereco());
            }
            if(clienteold.getTelefones().size() > cliente.getTelefones().size()){
            	for(Telefone t : clienteold.getTelefones()){
            		if(!cliente.getTelefones().contains(t)){
            			em.remove(t);
            		}
            	}
            }
            cliente = em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Cliente.class, cliente.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("O cliente " + cliente.getNome() + " não existe.");
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
            	throw new EntidadeNaoExistenteException("O cliente " + cliente.getNome() + " não existe.");
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
    
    public void removeAllEnderecos(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM Endereco WHERE id > 0;", Endereco.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void removeAllTelefones(){
    	EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM cliente_telefone WHERE telefone_id > 0;").executeUpdate();
            em.createNativeQuery("DELETE FROM Telefone WHERE id > 0;", Telefone.class).executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public int getQuantidadeClientes() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Cliente.class);
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
    
    public int getQuantidadePromocoes() {
        EntityManager em = getEntityManager();
        try {
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
