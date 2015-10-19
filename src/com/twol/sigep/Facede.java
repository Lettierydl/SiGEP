package com.twol.sigep;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.twol.sigep.controller.ControllerConfiguracao;
import com.twol.sigep.controller.ControllerEstoque;
import com.twol.sigep.controller.ControllerImpressora;
import com.twol.sigep.controller.ControllerLogin;
import com.twol.sigep.controller.ControllerPagamento;
import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.ControllerRelatorios;
import com.twol.sigep.controller.ControllerVenda;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.controller.find.FindPagamento;
import com.twol.sigep.controller.find.FindProduto;
import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.controller.gerador.GeradorPDF;
import com.twol.sigep.controller.gerador.GeradorPlanilha;
import com.twol.sigep.model.configuracoes.PermissaoFuncionario;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.MySQLBackup;
import com.twol.sigep.util.Persistencia;
import com.twol.sigep.util.SessionUtil;

public class Facede {

	private ControllerEstoque est;
	private ControllerPessoa pes;
	private ControllerLogin lg;
	private ControllerPagamento pagam;
	private ControllerVenda vend;
	private ControllerRelatorios rel;
	private GeradorPDF pdf;
	private GeradorPlanilha pla;
	private ControllerImpressora imp;

	public Facede() {
		est = new ControllerEstoque(Persistencia.emf);
		pes = new ControllerPessoa(Persistencia.emf);
		pagam = new ControllerPagamento(Persistencia.emf);
		vend = new ControllerVenda(Persistencia.emf);
		new ControllerConfiguracao(Persistencia.emf);
		rel = new ControllerRelatorios();
		lg = new ControllerLogin();
		pdf = new GeradorPDF(rel);
		pla = new GeradorPlanilha(rel);
		
		Funcionario logado = SessionUtil.getFuncionarioLogado();
		vend.setLogado(logado);
		
		try{
			imp = ControllerImpressora.getInstance();
		}catch(Error | Exception e){}
	}

	public void adicionarCliente(Cliente c) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_CLIENTES);
		pes.create(c);
	}

	public void removerCliente(Cliente c) throws EntidadeNaoExistenteException {
		pes.destroy(c);
	}

	public void atualizarCliente(Cliente c)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException ,Exception {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CLIENTES);
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
	
	public List<Cliente> buscarClientePorCPFOuNomeQueIniciam(String cpfOuNome, int maxResult) {
		return FindCliente.clientesQueNomeOuCPFIniciam(cpfOuNome, maxResult);
	}

	public List<String> buscarNomeClientePorNomeQueInicia(String nome) {
		return FindCliente.nomeClientesQueNomeInicia(nome);
	}
	
	public Cliente buscarClientePorNome(String nome) {
		return FindCliente.clienteComNome(nome);
	}

	// Verificar necessidade
	public Cliente buscarClientePorCPF(String cpf) {
		return FindCliente.clienteComCPF(cpf);
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
			TipoDeFuncionario tipoDeFuncionario) throws SenhaIncorretaException, FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_FUNCIONARIO);
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
	
	public Funcionario buscarFuncionarioPorNome(String nome) {
		return FindFuncionario.funcionarioComNome(nome);
	}
	
	public Funcionario buscarFuncionarioPorCPF(String cpf) {
		return FindFuncionario.funcionarioComCPF(cpf);
	}
	
	public Funcionario buscarFuncionarioPorLogin(String login) {
		return FindFuncionario.funcionarioComLogin(login);
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
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException,Exception {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_FUNCIONARIO);
		pes.edit(f);
	}

	public Funcionario buscarUsuarioPorNome(String nome) {
		return FindFuncionario.funcionarioComNome(nome);
	}

	/*
	 * Metodos de produto
	 */
	public void adicionarProduto(Produto p) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_PRODUTO);
		est.create(p);
	}

	public void removerProduto(Produto p) throws EntidadeNaoExistenteException {
		est.destroy(p);
	}

	public void atualizarProduto(Produto p)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException,Exception {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_PRODUTO);
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
	
	public List<String> buscarDescricaoProdutoPorDescricaoQueInicia(String descricao, int maxReult){
		return FindProduto.drecricaoProdutoQueIniciam(descricao, maxReult);
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
	
	public Produto buscarProdutoPorDescricao(String descricao){
		return FindProduto.produtoComDescricao(descricao);
	}
	
	public Produto buscarProdutoPorDescricaoOuCodigo(String nomeOuCodigo) {
		return  FindProduto.produtoComCodigoEDescricao(nomeOuCodigo);
	}
	
	public List<Produto> buscarListaProdutoPorDescricaoOuCodigo(String nomeOuCodigo) {
		return  FindProduto.produtosQueDescricaoOuCodigoDeBarrasIniciam(nomeOuCodigo);
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
	
	public void refreshValorDeVendaAtual() throws EntidadeNaoExistenteException, Exception{
		vend.refreshValorVendaAtual();
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
	
	
	public void adicionarDivida(Divida d, Cliente c) throws EntidadeNaoExistenteException, Exception{
		c.acrecentarDebito(d.getTotal());
		pes.edit(c);
		vend.create(d);
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
	public List<Pagamento> getListaPagamentoDoCliente(Cliente c, Date diaInicio, Date diaFim) {
		return FindPagamento.pagamentosDoCliente(c,  diaInicio,  diaFim);
	}

	public List<Pagamento> getListaPagamentoDosClientes(List<Cliente> clientes) {
		return FindPagamento.pagamentosDosClientes(clientes);
	}

	public double adicionarPagamento(Pagamento pagamento)
			throws EntidadeNaoExistenteException, ParametrosInvalidosException, Exception {
		Cliente c = FindCliente.clienteComId(pagamento.getCliente().getId());
		double troco= 0;
		if(c.getDebito() < pagamento.getValor()){
			troco = c.getDebito() - pagamento.getValor();
			pagamento.setValor(c.getDebito());
		}
		pagam.create(pagamento);
		vend.abaterValorDoPagamentoNaVenda(pagamento);
		c.diminuirDebito(pagamento.getValor());
		pes.edit(c);
		
		return troco;
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
	
	public List<ItemDeVenda> buscarItensDaVendaPorIdDaVenda(int id) {
		return FindVenda.itemDeVendaIdDaVenda(id);
	}
	
	public List<Pagavel> buscarPagaveisDoCliente(Cliente cliente,Date diaInicio, Date diaFim) {
		return FindVenda.pagavelCliente(cliente, diaInicio, diaFim);
	}
	
	
	public List<Pagavel> buscarPagaveisNaoPagosDosClientes(List<Cliente> clientes) {
		return FindVenda.pagavelNaoPagoDosClientes(clientes);
	}

	public List<Pagavel> buscarPagaveisNaoPagoDoCliente(Cliente cliente) {
		return FindVenda.pagavelNaoPagoDoCliente(cliente);

	}
	
	
	
	
	
	public boolean getValor(String chave, TipoDeFuncionario tipo){
		return ControllerConfiguracao.getValor(chave, tipo);
	}
	
	public void putValor(String chave, boolean valor,
			TipoDeFuncionario tipo){
		ControllerConfiguracao.putValor(chave, valor, tipo);
	}
	
	public void permissoesDeFuncionariosDefalt(){
		PermissaoFuncionario.configuracoesDefalt();
	}
	
	//balanco
	public String getMinDiaRegistroVenda(){
		return rel.getMinDia();
	}
	
	 public double[] getRelatorioDeEntradaDeCaixa(Date diaInicio,
				Date diaFim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioDeEntradaDeCaixa(diaInicio, diaFim);
	 }

	public double[] getRelatorioDeVendas(Date diaInicio, Date diaFim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioDeVendas(diaInicio, diaFim);
	}

	public double[] getRelatorioDeProduto(Date diaInicio, Date diaFim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioProduto(diaInicio, diaFim);
	}
	
	public double getRelatorioDeProduto30Dias(int idProduto){
		return rel.getRelatorioDeProduto30Dias(idProduto);
	}
	
	public String gerarPdfRelatorioBalancoProdutos(Date inicio, Date fim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pdf.gerarPdfRelatorioBalancoProdutos(inicio, fim);
	}

	public String gerarPdfDaVendaVenda(Venda v, List<ItemDeVenda> itens) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pdf.gerarPdfDaVenda(v, itens);
	}

	
	public String gerarPlanilhaRelatorioBalancoProdutos(Date inicio, Date fim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pla.gerarPlanilhaRelatorioBalancoProdutos(inicio, fim);
	}

	public void resteurarBancoDeDados(File tempFile) throws FuncionarioNaoAutorizadoException, IOException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CONFIGURACOES);
		MySQLBackup my = new MySQLBackup();
		my.restore(tempFile);
	}
	
	
	public String realizarBackupBancoDeDados(File file) throws FuncionarioNaoAutorizadoException, IOException {
		Funcionario logado = FindFuncionario.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CONFIGURACOES);
		MySQLBackup my = new MySQLBackup();
		return my.dump(file);
	}
	
	/* Apaga todas as vendas já pagas antes do dia informado 
	 * para melhorar o desempenho do banco de dados.
	 * */
	public int limparBancoDeDados(Date dia){
		return vend.limparBancoDeDados(dia);
	}
	
	
	public boolean imprimirVenda(Venda v){
		return imp.imprimirVenda(v);
	}
}
