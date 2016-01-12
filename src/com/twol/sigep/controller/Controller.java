package com.twol.sigep.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.twol.sigep.util.Persistencia;

public abstract class Controller {
	
	protected EntityManagerFactory emf = null;
	protected EntityManager em = null;
	
	protected EntityManager getEntityManager() {
		/*if (em != null && em.isOpen()) {
			//em.close();
		}*/
		if(em == null || !em.isOpen()){
			em = Persistencia.getEntityManager();
		}
		
		return em;
	}
	
}
