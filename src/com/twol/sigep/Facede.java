package com.twol.sigep;

import java.util.List;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeJaPersistidaException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.Persistencia;


public class Facede {

	
	/*--------------------
	 * Metodos do cliente 
	 ---------------------*/
	

	public void adicionarCliente(Cliente c){
		 Cliente.salvar(c);
	}
	
	public void removerCliente (Cliente c){
		Cliente.remover(c);
	}
	
	public void modificarCliente (Cliente c){// (Cliente antes, Cliente depois){
		Cliente.atualizar(c);
	}
	
	public Cliente buscarClientePorId(int idCliente){
		return Cliente.recuperarCliente(idCliente);
		
	}
	
	public List<Cliente> getListaClientes(){
		return Cliente.recuperarLista();
	}
	
	//Verificar necessidade
	public Cliente buscarClientePorCPF(String cpf){
		return null;
	}
	
	public void adicionarDependente(Dependente d){
		Dependente.salvar(d);
	}
	
	public void removerDependendte (Dependente d){
		Dependente.remover(d);
		
	}
	
	public List<Dependente> getListaDependentes(){
		return Dependente.recuperarLista();
	}
	
	/*-------------------------
	 * Metodos do Usu��rio 
	 --------------------------*/
			
	public void adicionarFuncionario(Funcionario u){
		//TODO Adicionar funcionario
	}
	
	public void removerFuncionario (Funcionario u){
		Funcionario.remover(u);
	}
	
	public void modificarFuncionario (Funcionario ant, Funcionario novo){
		//TODO modificar funcionario
	}
	public Funcionario buscarUsuarioPorNome(String nome){
		//TODO Buscar funcionario
		return null;
	}

	public Funcionario buscarUsuarioPorCPF(String cpf){
		//TODO 
		return null;
	}

	/*
	 * Metodos do produto
	 */
	public void adicionarProduto(Produto p) throws EntidadeJaPersistidaException{
		Produto.salvar(p);
	}
	
	public void removerProduto (Produto p){
		Produto.remover(p);
	}
	
	public void modificarProduto (Produto p){
		Produto.atualizar(p);
	}
	public Produto buscarProdutoPorDescricao(int idProduto){
		return Produto.recuperarProduto(idProduto);
	}

	public Funcionario buscarProdutoPorCodigo(String codigo){
		//TODO
		return null;
	}

	/*
	 * Negocio
	 */
	
	
	/*
	 * Efetua login a partir de uma senha e um login cadastrado no sistema, 
	 * caso o login não exista, lan��a exce����o
	 * caso senha de errado, retorna falso
	 * �� um metodo necessario para efetuar boa parte das outras operacoes de negocio
	 * 
	 */
	public boolean login (String login, String senha){
		return false;
	}
	
	/*
	 * Este metodo deve limpar todas as tabelas do banco de dados, 
	 * serve para possibilitar os testes 
	 */
	public void limparBancoDeDados(){
		Persistencia.limparBancoDeDados();
	}
	
	/*
	 * Encerra as atividades do sistema,
	 *  impossibilita que sejam efetuadas qualquer atividades do sistema
	 */
	public void logoff (){//utiliza o ControllerFuncionario
		
	}

	/*
	 * retorna o usuario que esta logado no momento de invocacao do metodo
	 */
	public Funcionario getFuncionarioLogado(){
		return null;
	}
	
	
	/*
	 * metodo para iniciar o processo de venda
	 */
	public void inicializarVenda (){
		
	}
	/*
	 * metodo utilizado para finalizar a venda
	 */
	public void finalizarVenda( Cliente cliente , Funcionario usuario , String dependente, List<Produto> produtos){
		
		
	}



	
	
	
	
	
	
	
	
	
	




}

