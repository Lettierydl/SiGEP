package com.twol.sigep;

import java.util.List;

import com.twol.sigep.model.estoque.FinderProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.ControllerFuncionario;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.FinderCliente;
import com.twol.sigep.model.pessoas.FinderPagamento;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Pagamento;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.FinderVenda;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.Persistencia;


public class Facede {

	private ControllerFuncionario func;
	/*--------------------
	 * Metodos do cliente 
	 ---------------------*/
	public Facede(){
		func = new ControllerFuncionario();
	}

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
	
	public List<Cliente> buscarClientePorCPFOuNomeQueIniciam(String cpfOuNome){
		return FinderCliente.clientesQueNomeOuCPFIniciam(cpfOuNome);
	}
	
	public List<String> buscarNomeClientePorNomeQueInicia(String nome){
		return FinderCliente.nomeClientesQueNomeInicia(nome);
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
	 * Metodos do Usu������������������rio 
	 --------------------------*/
			
	public void adicionarFuncionario(Funcionario f, String senha , TipoDeFuncionario tipoDoFuncionario) throws ParametrosInvalidosException, PermissaoInvalidaException{
		func.salvarFuncionario(f, senha, tipoDoFuncionario);
	}
	
	public List<Funcionario> buscarFuncionarioPorNomeQueInicia(String nome){
		return Funcionario.recuperarFuncionarioPorNomeQueInicia(nome);
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
	public void adicionarProduto(Produto p){
		Produto.salvar(p);
	}
	
	public void removerProduto (Produto p){
		Produto.remover(p);
	}
	
	public void modificarProduto (Produto p){
		Produto.atualizar(p);
	}
	
	public List<Produto> getListaProdutos(){
		return Produto.recuperarLista();
	}
	
	public List<Produto> buscarProdutoPorDescricaoQueInicia(String descricao){
		return FinderProduto.produtosQueDescricaoLike(descricao);
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
	 * caso o login n������o exista, lan������������������a exce������������������������������������o
	 * caso senha de errado, retorna falso
	 * ������������������ um metodo necessario para efetuar boa parte das outras operacoes de negocio
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
	
	public List<Funcionario> getFuncionarios(){
		return Funcionario.recuperarLista();
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

	public List<Pagamento> getListaPagamentoHoje() {
		return FinderPagamento.pagamentosDeHoje();
	}
	
	public List<Pagamento> getListaPagamentoDoCliente(Cliente c) {
		return FinderPagamento.pagamentosDoCliente(c);
	}
	
	public List<Pagamento> getListaPagamentoDosClientes(List<Cliente> clientes) {
		return FinderPagamento.pagamentosDosClientes(clientes);
	}

	public void adicionarPagamento(Pagamento pagamento) {
		Pagamento.salvar(pagamento);
	}

	public List<Venda> getListaVendasNaoPagasDeHoje() {
		return FinderVenda.vendasNaoPagaDeHoje();
	}

	



	
	
	
	
	
	
	
	
	
	




}

