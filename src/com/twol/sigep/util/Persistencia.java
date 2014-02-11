package com.twol.sigep.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
	
	public static void restartConnection(){
		 try{
			 em.close();
		 }catch(Exception e){}
		 em = emf.createEntityManager();
	}
	
	
	public static void limparBancoDeDados(){
		restartConnection();
		executeNativeQuery("DELETE FROM Cliente_telefone");
		executeNativeQuery("DELETE FROM Funcionario_telefone");
		executeNativeQuery("DELETE FROM Representante_telefone");
		executeNativeQuery("DELETE FROM Cliente");
		executeNativeQuery("DELETE FROM Dependente");
		executeNativeQuery("DELETE FROM Endereco");
		executeNativeQuery("DELETE FROM Funcionario");
		executeNativeQuery("DELETE FROM Linha_da_venda");
		executeNativeQuery("DELETE FROM Produto");
		executeNativeQuery("DELETE FROM Promocao");
		executeNativeQuery("DELETE FROM Representante");
		executeNativeQuery("DELETE FROM Telefone");
		executeNativeQuery("DELETE FROM Venda");
		restartConnection();
	}

	private static void executeNativeQuery(String sql) {
		iniciarTrascao();
		em.createNativeQuery(sql).executeUpdate();
		finalizarTrascao();
	}

	public static void flush() {
		iniciarTrascao();
		em.flush();
	}
	
}
