package com.twol.sigep;

import java.util.List;

import com.twol.sigep.controller.ControllerEstoque;
import com.twol.sigep.controller.ControllerLogin;
import com.twol.sigep.controller.ControllerPagamento;
import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.ControllerVenda;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.controller.find.FindPagamento;
import com.twol.sigep.controller.find.FindProduto;
import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.Persistencia;
import com.twol.sigep.util.SessionUtil;

public class Facede {

	private ControllerEstoque est;
	private ControllerPessoa pes;
	private ControllerLogin lg;
	private ControllerPagamento pagam;
	private ControllerVenda vend;

	public Facede() {
		est = new ControllerEstoque(Persistencia.emf);
		pes = new ControllerPessoa(Persistencia.emf);
		pagam = new ControllerPagamento(Persistencia.emf);
		vend = new ControllerVenda(Persistencia.emf);
		lg = new ControllerLogin();
		
		Funcionario logado = SessionUtil.getFuncionarioLogado();
		vend.setLogado(logado);
	}

	public void adicionarCliente(Cliente c) {
		pes.create(c);
	}

	public void removerCliente(Cliente c) throws EntidadeNaoExistenteException {
		pes.destroy(c);
	}

	public void atualizarCliente(Cliente c)
			throws EntidadeNaoExistenteException, Exception {
		pes.edit(c);
	}

	public Cliente buscarClientePorId(int id) {
		return FindCliente.clienteComId(id);
	}

	public List<Cliente> getListaClientes() {
		return FindCliente.listCelientes();
	}

	public Cliente buscarClientePorCPFOuNomeIqualA(String cpfOuNome){
		return FindCliente.clientesQueNomeOuCPFIqualA(cpfOuNome);
	}
	
	public List<Cliente> buscarClientePorCPFOuNomeQueIniciam(String cpfOuNome) {
		return FindCliente.clientesQueNomeOuCPFIniciam(cpfOuNome);
	}

	public List<String> buscarNomeClientePorNomeQueInicia(String nome) {
		return FindCliente.nomeClientesQueNomeInicia(nome);
	}

	// Verificar necessidade
	public Cliente buscarClientePorCPF(String cpf) {
		return FindCliente.clientesComCPF(cpf);
	}

	public void adicionarDependente(Dependente d) {
		Dependente.salvar(d);
	}

	public void removerDependendte(Dependente d) {
		Dependente.remover(d);

	}

	public List<Dependente> getListaDependentes() {
		return Dependente.recuperarLista();
	}

	/*-------------------------
	 * Metodos do Usu��rio/Funcionario
	 --------------------------*/

	public List<Funcionario> getListaFuncionarios() {
		return FindFuncionario.listFuncionarios();
	}

	public void adicionarFuncionario(Funcionario f, String senha,
			TipoDeFuncionario tipoDeFuncionario) throws SenhaIncorretaException {
		lg.atribuirSenhaETipoAoFuncionario(f, senha, tipoDeFuncionario);
		pes.create(f);
	}
	
	/*Não salva o funcionario no banco*/
	public void alterarSenhaDoFuncionario(Funcionario f, String senha, String novaSenha) throws SenhaIncorretaException, LoginIncorretoException{
		lg.alterarSenhaDoFuncionario(f, senha, novaSenha);
	}
	
	public List<Funcionario> buscarFuncionarioPorNomeQueInicia(String nome) {
		return FindFuncionario.funcionariosQueNomeInicia(nome);
	}
	
	public Funcionario buscarFuncionarioPorId(int id) {
		return FindFuncionario.funcionarioComId(id);
	}

	public void removerFuncionario(Funcionario u)
			throws EntidadeNaoExistenteException {
		pes.destroy(u);
	}

	public Funcionario buscarFuncionarioPeloLoginESenha(String login,
			String senha) {
		return FindFuncionario.funcionarioComLoginESenha(login, senha);
	}

	public void atualizarFuncionario(Funcionario f)
			throws EntidadeNaoExistenteException, Exception {
		pes.edit(f);
	}

	public Funcionario buscarUsuarioPorNome(String nome) {
		return FindFuncionario.funcionarioComNome(nome);
	}

	/*
	 * Metodos de produto
	 */
	public void adicionarProduto(Produto p) {
		est.create(p);
	}

	public void removerProduto(Produto p) throws EntidadeNaoExistenteException {
		est.destroy(p);
	}

	public void atualizarProduto(Produto p)
			throws EntidadeNaoExistenteException, Exception {
		est.edit(p);
	}

	public List<Produto> getListaProdutos() {
		return FindProduto.todosProdutos();
	}

	public List<Produto> buscarProdutoPorDescricaoQueInicia(String descricao) {
		return FindProduto.produtosQueDescricaoLike(descricao);
	}
	
	public List<Produto> buscarProdutoPorDescricaoOuCodigoQueInicia(String descricaoOuCodigo){
		return FindProduto.produtosQueDescricaoOuCodigoDeBarrasIniciam(descricaoOuCodigo);
	}
	
	public List<String> buscarDescricaoProdutoPorDescricaoQueInicia(String descricao){
		return FindProduto.drecricaoProdutoQueIniciam(descricao);
	}
	
	public List<String> buscarCodigoProdutoPorCodigoQueInicia(String codigo){
		return FindProduto.codigoProdutoQueIniciam(codigo);
	}

	public Produto buscarProdutoPorId(int idProduto) {
		return FindProduto.produtoComId(idProduto);
	}

	public Produto buscarProdutoPorCodigo(String codigo) {
		return FindProduto.produtoComCodigoDeBarras(codigo);
	}
	
	public Produto buscarProdutoPorDescricaoOuCodigo(String nomeOuCodigo) {
		return  FindProduto.produtoComCodigoEDescricao(nomeOuCodigo);
	}
	
	/*
	 * Negocio
	 */

	/**
	 * Efetua login de um funcionario no sistema com o login e senha dele<br/>
	 * 
	 * @exception SenhaIncorretaException
	 *                Funcionario existente porem a senha esta incorreta
	 * @exception LoginIncorretoException
	 *                Login do funcionario n��o existe caso senha de errado,
	 *                retorna falso
	 */
	public void login(String login, String senha)
			throws SenhaIncorretaException, LoginIncorretoException {
		lg.logar(login, senha);
		vend.setLogado(lg.getLogado());
		SessionUtil.putFuncionarioLogado(lg.getLogado());
	}

	/**
	 * Encerra as atividades do sistema,<br/>
	 * impossibilita que sejam efetuadas qualquer atividades do sistema
	 */
	public void logoff() {// utiliza o ControllerFuncionario
		lg.logoff();
		vend.setLogado(null);
	}

	/**
	 * Retorna o usuario que esta logado no momento de invocacao do metodo
	 */
	public Funcionario getFuncionarioLogado() {
		return lg.getLogado();
	}

	public List<Funcionario> getFuncionarios() {
		return FindFuncionario.listFuncionarios();
	}

	/*
	 * metodo para iniciar o processo de venda
	 */
	public void inicializarVenda() throws VendaPendenteException {
		vend.iniciarNovaVenda();
	}
	
	public void addItemAVenda(ItemDeVenda it) throws EntidadeNaoExistenteException, Exception {
		vend.addItem(it);
	}
	
	public void removerItemDaVenda(ItemDeVenda it) throws EntidadeNaoExistenteException, Exception {
		vend.removerItem(it);
	}
	
	public Venda getVendaAtual(){
		return vend.getAtual();
	}
	
	public void atualizarDataVendaAtual() throws EntidadeNaoExistenteException, Exception{
		vend.atualizarDataVendaAtual();
	}
	
	/**
	 * Metodo utilizado para finalizar uma venda a vista<br/>
	 * 
	 * 
	 * @param A venda a ser finalizada
	 * @throws Exception
	 * @throws EntidadeNaoExistenteException
	 */
	public void finalizarVendaAVista(Venda v)
			throws EntidadeNaoExistenteException, Exception {
		vend.finalizarVendaAVista(v);
	}
	
	/**
	 * Metodo utilizado para finalizar uma venda a prazo<br/>
	 * 
	 * @return Retorna o valor da conta do cliente
	 * @param Venda e Valor que o cliente pagou
	 * @throws Exception
	 * @throws EntidadeNaoExistenteException
	 */
	public double finalizarVendaAprazo(Venda v, Cliente c, double partePaga)
			throws EntidadeNaoExistenteException, Exception {
		c.acrecentarDebito(vend.finalizarVendaAPrazo(v, c, partePaga));
		pes.edit(c);
		return c.getDebito();
	}

	public Venda recuperarVendaPendente() throws VariasVendasPendentesException, EntidadeNaoExistenteException, Exception{
		return vend.recuperarVendaPendente();
	}
	
	public void selecionarVendaPendente(int idVenda) throws  EntidadeNaoExistenteException, Exception{
		vend.selecionarVendaPendente(idVenda);
	}
	
	public void removerVendaPendente(Venda v) throws EntidadeNaoExistenteException{
		vend.removerVendaPendente(v);
	}
	
	public void setAtualComVendaPendenteTemporariamente(){
		vend.setAtualComVendaPendenteTemporariamente();
	}
	
	public List<Pagamento> getListaPagamentoHoje() {
		return FindPagamento.pagamentosDeHoje();
	}

	public List<Pagamento> getListaPagamentoDoCliente(Cliente c) {
		return FindPagamento.pagamentosDoCliente(c);
	}

	public List<Pagamento> getListaPagamentoDosClientes(List<Cliente> clientes) {
		return FindPagamento.pagamentosDosClientes(clientes);
	}

	public double adicionarPagamento(Pagamento pagamento)
			throws ParametrosInvalidosException {
		pagam.create(pagamento);
		// falta chamar o outro metodo de venda
		// falta atualizar o debito do cliente
		return 0;
	}

	public List<Venda> getListaVendasNaoPagasDeHoje() {
		return FindVenda.vendasNaoPagaDeHoje();
	}

	public List<Venda> buscarVendasNaoPagasDosClientes(List<Cliente> clientes) {
		return FindVenda.vendasNaoPagasDosClientes(clientes);
	}

	public List<Venda> buscarVendaNaoPagaDoCliente(Cliente cliente) {
		return FindVenda.vendasNaoPagaDoCliente(cliente);

	}
}
