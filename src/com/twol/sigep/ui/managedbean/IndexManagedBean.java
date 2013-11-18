package com.twol.sigep.ui.managedbean;


import javax.faces.bean.ManagedBean;

import com.twol.sigep.model.pessoas.Cliente;

@ManagedBean(name = "indexManagedBean")
public class IndexManagedBean {
	
	
	public IndexManagedBean(){
		
	}
	
	
	public void listarClientes(){
		System.out.println("Chamou Metodo");
		for(Cliente c : Cliente.recuperarLista()){
			System.out.println(c);
		}
                Cliente c = new Cliente();
                c.setCpf("1231231231");
                c.setNome("ClienteTeste");
                Cliente.salvar(c);
	}
	
        public String getTeste(){
            return "Teste";
        }
}
