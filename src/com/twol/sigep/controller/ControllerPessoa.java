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
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Funcionario;
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
            Funcionario funcionarioold = em.getReference(Funcionario.class, funcionario.getId());
            if(funcionarioold.getEndereco()!=null && funcionario.getEndereco()==null){
            	em.remove(funcionarioold.getEndereco());
            }
            if(funcionarioold.getTelefones().size() > funcionario.getTelefones().size()){
            	for(Telefone t : funcionarioold.getTelefones()){
            		if(!funcionario.getTelefones().contains(t)){
            			em.remove(t);
            		}
            	}
            }
            funcionario = em.merge(funcionario);
            em.getTransaction().commit();
        } catch (Exception ex) {
        	try{
        		em.find(Funcionario.class, funcionario.getId());
        	}catch(EntityNotFoundException enfe){
        		throw new EntidadeNaoExistenteException("O funcionário " + funcionario.getNome() + " não existe.");
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
            	throw new EntidadeNaoExistenteException("A funcionário " + funcionario.getNome() + " não existe.");
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
            em.createNativeQuery("DELETE FROM funcionario_telefone WHERE telefone_id > 0;").executeUpdate();
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
   
    public int getQuantidadeFuncionarios() {
        EntityManager em = getEntityManager();
        try {
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
