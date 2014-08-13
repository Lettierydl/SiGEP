package com.twol.sigep;

import java.util.List;

import com.twol.sigep.controller.ControllerEstoque;
import com.twol.sigep.model.estoque.FinderProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.ControllerFuncionario;
import com.twol.sigep.model.pessoas.ControllerPagamento;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.FinderCliente;
import com.twol.sigep.model.pessoas.FinderPagamento;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Pagamento;
import com.twol.sigep.model.pessoas.Representante;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.FinderVenda;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.Persistencia;


public class Facede {
	
	private ControllerEstoque est;
	private ControllerFuncionario func;
	private ControllerPagamento pagam;
	/*--------------------
	 * Metodos do cliente 
	 ---------------------*/
	public Facede(){
		est= new ControllerEstoque(Persistencia.emf);
		func = new ControllerFuncionario();
		pagam = new ControllerPagamento();
	}

	public void adicionarCliente(Cliente c){
		 Cliente.salvar(c);
	}
	
	public void removerCliente (Cliente c){
		Cliente.remover(c);
	}
	
	public void atualizarCliente (Cliente c){
		Cliente.atualizar(c);
	}
	
	public Cliente buscarClientePorId(int id){
		return Cliente.recuperarCliente(id);
		
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
			
	public List<Funcionario> getListaFuncionarios() {
		return Funcionario.recuperarLista();
	}
	
	public void adicionarFuncionario(Funcionario f, String senha , TipoDeFuncionario tipoDoFuncionario) throws ParametrosInvalidosException, PermissaoInvalidaException{
		func.salvarFuncionario(f, senha, tipoDoFuncionario);
	}
	
	public List<Funcionario> buscarFuncionarioPorNomeQueInicia(String nome){
		return Funcionario.recuperarFuncionarioPorNomeQueInicia(nome);
	}
	
	public void removerFuncionario (Funcionario u){
		Funcionario.remover(u);
	}
	
	public Funcionario buscarFuncionarioPeloLoginESenha(String login, String senha) {
		return Funcionario.recuperarFuncionarioPorLoginESenha(login, senha);
	}
	
	public void atualizarFuncionario (Funcionario f){
		func.atualizarFuncionario(f);
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
		est.create(p);
	}
	
	public void removerProduto (Produto p) throws EntidadeNaoExistenteException{
		est.destroy(p);
	}
	
	public void atualizarProduto (Produto p) throws EntidadeNaoExistenteException, Exception{
		est.edit(p);
	}
	
	public List<Produto> getListaProdutos(){
		return FinderProduto.todosProdutos();
	}
	
	public List<Produto> buscarProdutoPorDescricaoQueInicia(String descricao){
		return FinderProduto.produtosQueDescricaoLike(descricao);
	}
	
	public Produto buscarProdutoPorId(int idProduto){
		return FinderProduto.produtoComId(idProduto);
	}

	public Produto buscarProdutoPorCodigo(String codigo){
		return FinderProduto.produtoComCodigoDeBarras(codigo);
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

	public double adicionarPagamento(Pagamento pagamento) throws ParametrosInvalidosException {
		return pagam.cadastrarPagamento(pagamento);
	}

	public List<Venda> getListaVendasNaoPagasDeHoje() {
		return FinderVenda.vendasNaoPagaDeHoje();
	}

	public List<Venda> buscarVendasNaoPagasDosClientes(List<Cliente> clientes) {
		return FinderVenda.vendasNaoPagasDosClientes(clientes);
	}

	public List<Venda> buscarVendaNaoPagaDoCliente(Cliente cliente) {
		return FinderVenda.vendasNaoPagaDoCliente(cliente);
	
	}
	
	//Representante

	public List<Representante> getListaRepresentantes() {
		return Representante.recuperarLista();
	}

	public void adicionarRepresentante(Representante r){
		Representante.salvar(r);
	}
	
	public void removerRepresentante(Representante r){
		Representante.remover(r);
	}
	
	public void atualizarRepresentante(Representante r){
		Representante.atualizar(r);
	}
	
	public Representante buscarRepresentantePeloId(int id){
		return Representante.recuperarRepresentante(id);
	}



	
	
	
	
	
	
	
	
	
	




}

