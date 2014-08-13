package com.twol.sigep.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.Promocao;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Representante;
import com.twol.sigep.model.vendas.LinhaDaVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;

public class Persistencia {
	public static final String UNIDADE_DE_PERSISTENCIA = "SiGEPPU";

	public static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory(UNIDADE_DE_PERSISTENCIA);
	public static EntityManager em = emf.createEntityManager();
	
	public static void iniciarTrascao(){
		restartConnection();
		em.getTransaction().begin();
	}
	
	public static void finalizarTrascao(){
		if(em.getTransaction().isActive()){
			em.getTransaction().commit();
		}
		em.close();
	}
	
	public static void rollBackTrascao(){
		em.getTransaction().rollback();
	}
	
	public static void restartConnection(){
		 try{
			 em.close();
		 }catch(Exception e){}
		 em = emf.createEntityManager();
	}
	
	
	public static void limparBancoDeDados(){
		restartConnection();
		for(Pagamento c : Pagamento.recuperarLista()){
			Pagamento.remover(c);
		}
		for(Representante c : Representante.recuperarLista()){
			Representante.remover(c);
		}
		
		for(Venda p : Venda.recuperarLista()){
			Venda.remover(p);
		}
		for(LinhaDaVenda p : LinhaDaVenda.recuperarLista()){
			LinhaDaVenda.remover(p);
		}
		
		restartConnection();
	}

	private static void executeNativeQuery(String sql) {
		iniciarTrascao();
		em.createNativeQuery(sql).executeUpdate();
		finalizarTrascao();
	}
	
	private static void executeQuery(String sql) {
		iniciarTrascao();
			try{
				em.createNativeQuery(sql).executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				finalizarTrascao();
			}
	}

	public static void flush() {
		iniciarTrascao();
		em.flush();
	}
	
}
