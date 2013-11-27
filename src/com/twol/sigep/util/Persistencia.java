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
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public static void finalizarTrascao(){
		try{
			em.getTransaction().commit();
		}finally{
			em.close();
		}
	}
	
	public static void restartConnection(){
		 try{
			 em.close();
		 }catch(Exception e){}
		 em = emf.createEntityManager();
	}
	
}
