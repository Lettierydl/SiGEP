package com.twol.sigep.ui.managedbean;


import javax.faces.bean.ManagedBean;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeJaPersistidaException;
import com.twol.sigep.model.pessoas.Cliente;

@ManagedBean(name = "indexBean")
public class IndexManagedBean {
	
	private Facede f;
	
	public IndexManagedBean(){
		
	}
	
	
	public void listarClientes() throws EntidadeJaPersistidaException{
		System.out.println("Chamou Metodo");
		for(Cliente c : f.getListaClientes()){
			System.out.println(c);
		}
                Cliente c = new Cliente();
                c.setCpf("1231231231");
                c.setNome("ClienteTeste");
                f.adicionarCliente(c);
	}
	
        public String getTeste(){
            return "Teste";
        }
}
