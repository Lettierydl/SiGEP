package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.Query;

import com.twol.sigep.util.Persistencia;

public class FinderPagamento {
	
	@SuppressWarnings("unchecked")
	public static List<Pagamento> pagamentosDeHoje() {
		
		String stringQuery = "select p FROM Pagamento as p ";
		stringQuery += "WHERE day(p.data) = day(curdate()) and p.data >= curdate()"
					+ " order by p.data DESC";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
		
		List<Pagamento> pagamentos = (List<Pagamento>) query.getResultList();
		return pagamentos;
	}
	
	
	public static List<Pagamento> pagamentosDoCliente(Cliente cliente) {
		Cliente.atualizar(cliente);
		Persistencia.flush();
		return cliente.getPagamentos();
	}


	public static List<Pagamento> pagamentosDosClientes(List<Cliente> clientes) {
		//vai rodando pelo cliente_id //for clientes cli{ p.cliente = cli or }
			String stringQuery = "select p FROM Pagamento as p ";
			stringQuery += "WHERE (p.cliente != NULL) ";
				for(int i = 0; i< clientes.size(); i++){
					if(i>0){
						stringQuery += "or p.cliente = :cli"+i+" ";
					}else{
						stringQuery += "and ( p.cliente = :cli"+i+" ";
					}
				}
				
				stringQuery+= ") order by p.data DESC";
				
				Persistencia.restartConnection();
				Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
				
				for(int i = 0; i< clientes.size(); i++){
					query.setParameter("cli"+i, clientes.get(i));
				}
				
				@SuppressWarnings("unchecked")
				List<Pagamento> pagamentos = (List<Pagamento>) query.getResultList();
				return pagamentos;
	}
	
	
	
	
	
	
}
