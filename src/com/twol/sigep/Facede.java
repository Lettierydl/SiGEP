package com.twol.sigep;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.twol.sigep.model.exception.EstadoInvalidoDaVendaAtualException;
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
import com.twol.sigep.util.Backup;
import com.twol.sigep.util.OperacaoStringUtil;
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
	private ControllerConfiguracao config;
	
	private FindVenda fv;
	private FindProduto fprod;
	private FindPagamento fpag;
	private FindFuncionario ffunc;
	private FindCliente fcli;

	public Facede() {
		est = new ControllerEstoque();
		pes = new ControllerPessoa();
		pagam = new ControllerPagamento();
		vend = new ControllerVenda();
		new ControllerConfiguracao();
		rel = new ControllerRelatorios();
		lg = new ControllerLogin();
		pdf = new GeradorPDF(rel);
		pla = new GeradorPlanilha(rel);
		config = new ControllerConfiguracao();
		
		
		Funcionario logado = SessionUtil.getFuncionarioLogado();
		vend.setLogado(logado);
		
		fv = new FindVenda();
		fprod = new FindProduto();
		fpag = new FindPagamento();
		ffunc = new FindFuncionario();
		fcli = new FindCliente();
		
		try{
			imp = ControllerImpressora.getInstance();
		}catch(Error | Exception e){}
	}

	public void adicionarCliente(Cliente c) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_CLIENTES);
		pes.create(c);
	}

	public void removerCliente(Cliente c) throws EntidadeNaoExistenteException {
		pes.destroy(c);
	}

	public void atualizarCliente(Cliente c)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException ,Exception {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CLIENTES);
		pes.edit(c);
	}
	
	public double recalcularDebitoDoCliente(Cliente c) throws EntidadeNaoExistenteException, Exception {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CLIENTES);
		return pes.recalcularDebitoDoCliente(c);	
	}

	public Cliente buscarClientePorId(int id) {
		return fcli.clienteComId(id);
	}

	public List<Cliente> getListaClientes() {
		return fcli.listCelientes();
	}

	public Cliente buscarClientePorCPFOuNomeIqualA(String cpfOuNome){
		return fcli.clientesQueNomeOuCPFIqualA(cpfOuNome);
	}
	
	public List<Cliente> buscarClientePorCPFOuNomeQueIniciam(String cpfOuNome) {
		return fcli.clientesQueNomeOuCPFIniciam(cpfOuNome);
	}
	
	public List<Cliente> buscarClientePorCPFOuNomeQueIniciam(String cpfOuNome, int maxResult) {
		return fcli.clientesQueNomeOuCPFIniciam(cpfOuNome, maxResult);
	}

	public List<String> buscarNomeClientePorNomeQueInicia(String nome) {
		return fcli.nomeClientesQueNomeInicia(nome);
	}
	
	public Cliente buscarClientePorNome(String nome) {
		return fcli.clienteComNome(nome);
	}

	// Verificar necessidade
	public Cliente buscarClientePorCPF(String cpf) {
		return fcli.clienteComCPF(cpf);
	}

	/*Dependente*/
	
	public void adicionarDependente(Dependente d, Cliente c) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_CLIENTES);
		d.setCliente(c);
		pes.create(d);
	}

	public void removerDependente(Dependente d) throws EntidadeNaoExistenteException {
		pes.destroy(d);
	}

	public void atualizarDependente(Dependente d)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException ,Exception {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CLIENTES);
		pes.edit(d);
	}

	public Dependente buscarDependentePorId(int id) {
		return fcli.dependenteComId(id);
	}
	
	public Dependente buscarDependenteNomeECliente(String nomeDependente,
			Cliente c) {
		return fcli.dependenteComNomeClietne(nomeDependente, c);
	}

	public List<Dependente> getListaDependentes(Cliente c) {
		return fcli.listDependentes(c);
	}
	
	public List<String> getListaNomeDependentes(int idCliente) {
		return fcli.listNomeDependentes(idCliente);
	}
	
	
	/*-------------------------
	 * Metodos do Usu��rio/Funcionario
	 --------------------------*/

	public List<Funcionario> getListaFuncionarios() {
		return ffunc.listFuncionarios();
	}

	public void adicionarFuncionario(Funcionario f, String senha,
			TipoDeFuncionario tipoDeFuncionario) throws SenhaIncorretaException, FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_FUNCIONARIO);
		lg.atribuirSenhaETipoAoFuncionario(f, senha, tipoDeFuncionario);
		pes.create(f);
	}
	
	/*Não salva o funcionario no banco*/
	public void alterarSenhaDoFuncionario(Funcionario f, String senha, String novaSenha) throws SenhaIncorretaException, LoginIncorretoException{
		lg.alterarSenhaDoFuncionario(f, senha, novaSenha);
	}
	
	public List<Funcionario> buscarFuncionarioPorNomeQueInicia(String nome) {
		return ffunc.funcionariosQueNomeInicia(nome);
	}
	
	public Funcionario buscarFuncionarioPorNome(String nome) {
		return ffunc.funcionarioComNome(nome);
	}
	
	public Funcionario buscarFuncionarioPorCPF(String cpf) {
		return ffunc.funcionarioComCPF(cpf);
	}
	
	public Funcionario buscarFuncionarioPorLogin(String login) {
		return ffunc.funcionarioComLogin(login);
	}
	
	public Funcionario buscarFuncionarioPorId(int id) {
		return ffunc.funcionarioComId(id);
	}

	public void removerFuncionario(Funcionario u)
			throws EntidadeNaoExistenteException {
		pes.destroy(u);
	}

	public Funcionario buscarFuncionarioPeloLoginESenha(String login,
			String senha) {
		return ffunc.funcionarioComLoginESenha(login, senha);
	}

	public void atualizarFuncionario(Funcionario f)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException,Exception {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_FUNCIONARIO);
		pes.edit(f);
	}

	public Funcionario buscarUsuarioPorNome(String nome) {
		return ffunc.funcionarioComNome(nome);
	}

	/*
	 * Metodos de produto
	 */
	public void adicionarProduto(Produto p) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.CADASTRAR_PRODUTO);
		est.create(p);
	}

	public void removerProduto(Produto p) throws EntidadeNaoExistenteException {
		est.destroy(p);
	}

	public void atualizarProduto(Produto p)
			throws EntidadeNaoExistenteException, FuncionarioNaoAutorizadoException,Exception {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_PRODUTO);
		est.edit(p);
	}

	public List<Produto> getListaProdutos() {
		return fprod.todosProdutos();
	}
	
	public List<Object[]> getInformacaoProdutos(){
		return fprod.informacaoTodosProdutos();
	}

	public List<Produto> buscarProdutoPorDescricaoQueInicia(String descricao) {
		return fprod.produtosQueDescricaoLike(descricao);
	}
	
	public List<Produto> buscarProdutoPorDescricaoOuCodigoQueInicia(String descricaoOuCodigo){
		return fprod.produtosQueDescricaoOuCodigoDeBarrasIniciam(descricaoOuCodigo);
	}
	
	public List<String> buscarDescricaoProdutoPorDescricaoQueInicia(String descricao){
		return fprod.drecricaoProdutoQueIniciam(descricao);
	}
	
	public List<String> buscarDescricaoProdutoPorDescricaoQueInicia(String descricao, int maxReult){
		return fprod.drecricaoProdutoQueIniciam(descricao, maxReult);
	}
	
	public List<String> buscarCodigoProdutoPorCodigoQueInicia(String codigo){
		return fprod.codigoProdutoQueIniciam(codigo);
	}
	
	public List<String> buscarDescricaoEPrecoProdutoPorDescricaoQueInicia(String descricao, int maxReult){
		return fprod.drecricaoEPrecoProdutoQueIniciam(descricao, maxReult);
	}
	
	public List<String> buscarDescricaoEPrecoProdutoPorDescricaoQueInicia(String descricao){
		return fprod.drecricaoEPrecoProdutoQueIniciam(descricao);
	}

	public Produto buscarProdutoPorId(int idProduto) {
		return fprod.produtoComId(idProduto);
	}

	public Produto buscarProdutoPorCodigo(String codigo) {
		return fprod.produtoComCodigoDeBarras(codigo);
	}
	
	public Produto buscarProdutoPorDescricao(String descricao){
		return fprod.produtoComDescricao(descricao);
	}
	
	public Produto buscarProdutoPorDescricaoOuCodigo(String nomeOuCodigo) {
		return  fprod.produtoComCodigoEDescricao(nomeOuCodigo);
	}
	
	public List<Produto> buscarListaProdutoPorDescricaoOuCodigo(String nomeOuCodigo) {
		return  fprod.produtosQueDescricaoOuCodigoDeBarrasIniciam(nomeOuCodigo);
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
		return ffunc.listFuncionarios();
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
	 * @throws ParametrosInvalidosException 
	 */
	public synchronized double finalizarVendaAprazo(Venda v, Cliente c, double partePaga)
			throws ParametrosInvalidosException, EstadoInvalidoDaVendaAtualException {
		double deb = 0;
		
		try{
			deb = vend.finalizarVendaAPrazo(v, c, partePaga);
			
			double oud_deb = c.getDebito();
			
			try {
				c.acrecentarDebito(deb);
				pes.edit(c);
				if(this.buscarClientePorId(c.getId()).getDebito() != oud_deb + deb){
					throw new ParametrosInvalidosException("Falha na conexão com o banco de dados \nDebito não atualizado!");
				}
			} catch (Exception e) {
				if(this.buscarClientePorId(c.getId()).getDebito() != oud_deb + deb){
					List<Pagavel> di_ve = fv.pagavelNaoPagoDoCliente(c);
					double deb_real = 0;
					boolean existe = false;
					for(Pagavel p : di_ve){
						deb_real += p.getValorNaoPago();
						if(p instanceof Venda && p.getDia().equals(v.getDia()) && p.getTotal() == v.getTotal() ){
							existe = true;
						}
					}
					if(!existe){
						//venda não salva tentar salvar de novo
						deb = vend.finalizarVendaAPrazo(v, c, partePaga);
					}
					
					try {
						c.acrecentarDebito(c.getDebito() - deb_real);
						pes.edit(c);
						if(this.buscarClientePorId(c.getId()).getDebito() != deb_real){
							throw new ParametrosInvalidosException("Falha na conexão com o banco de dados \nDebito não atualizado!");
						}
					}catch (Exception e2) {
						e2.printStackTrace();
						throw new ParametrosInvalidosException("Falha na conexão com o banco de dados \nDebito não atualizado!");
					}
					
				}
			}
			
			
		}catch(EstadoInvalidoDaVendaAtualException ei){
			throw ei;
		}catch(Exception e){
			throw new EstadoInvalidoDaVendaAtualException("Venda não salva, verifique na tela de pagamentos!");
		}
		return this.buscarClientePorId(c.getId()).getDebito();
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
		return fpag.pagamentosDeHoje();
	}

	public List<Pagamento> getListaPagamentoDoCliente(Cliente c) {
		return fpag.pagamentosDoCliente(c);
	}
	public List<Pagamento> getListaPagamentoDoCliente(Cliente c, Date diaInicio, Date diaFim) {
		return fpag.pagamentosDoCliente(c,  diaInicio,  diaFim);
	}

	public List<Pagamento> getListaPagamentoDosClientes(List<Cliente> clientes) {
		return fpag.pagamentosDosClientes(clientes);
	}

	public double adicionarPagamento(Pagamento pagamento)
			throws EntidadeNaoExistenteException, ParametrosInvalidosException, Exception {
		Cliente c = fcli.clienteComId(pagamento.getCliente().getId());
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
		return fv.vendasNaoPagaDeHoje();
	}

	public List<Venda> buscarVendasNaoPagasDosClientes(List<Cliente> clientes) {
		return fv.vendasNaoPagasDosClientes(clientes);
	}

	public List<Venda> buscarVendaNaoPagaDoCliente(Cliente cliente) {
		return fv.vendasNaoPagaDoCliente(cliente);
	}
	
	public List<ItemDeVenda> buscarItensDaVendaPorIdDaVenda(int id) {
		return fv.itemDeVendaIdDaVenda(id);
	}
	
	public List<Pagavel> buscarPagaveisDoCliente(Cliente cliente,Date diaInicio, Date diaFim) {
		return fv.pagavelCliente(cliente, diaInicio, diaFim);
	}
	
	
	public List<Pagavel> buscarPagaveisNaoPagosDosClientes(List<Cliente> clientes) {
		return fv.pagavelNaoPagoDosClientes(clientes);
	}

	public List<Pagavel> buscarPagaveisNaoPagoDoCliente(Cliente cliente) {
		return fv.pagavelNaoPagoDoCliente(cliente);
	}
	
	public Venda buscarVendaPeloId(int id_venda) {
		return fv.vendaId(id_venda);
	}
	
	public boolean getValor(String chave, TipoDeFuncionario tipo){
		return config.getValor(chave, tipo);
	}
	
	public void putValor(String chave, boolean valor,
			TipoDeFuncionario tipo){
		config.putValor(chave, valor, tipo);
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
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioDeEntradaDeCaixa(diaInicio, diaFim);
	 }

	public double[] getRelatorioDeVendas(Date diaInicio, Date diaFim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioDeVendas(diaInicio, diaFim);
	}

	public double[] getRelatorioDeProduto(Date diaInicio, Date diaFim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return rel.getRelatorioProduto(diaInicio, diaFim);
	}
	
	public double getRelatorioDeProduto30Dias(int idProduto){
		return rel.getRelatorioDeProduto30Dias(idProduto);
	}
	
	public String gerarPdfRelatorioBalancoProdutos(Date inicio, Date fim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pdf.gerarPdfRelatorioBalancoProdutos(inicio, fim);
	}

	public String gerarPdfDaVendaVenda(Venda v, List<ItemDeVenda> itens) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pdf.gerarPdfDaVenda(v, itens);
	}

	
	public String gerarPlanilhaRelatorioBalancoProdutos(Date inicio, Date fim) throws FuncionarioNaoAutorizadoException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.GERAR_RELATORIOS);
		return pla.gerarPlanilhaRelatorioBalancoProdutos(inicio, fim);
	}

	public boolean resteurarBancoDeDados(File tempFile) throws FuncionarioNaoAutorizadoException, IOException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CONFIGURACOES);
		Backup bac = new Backup();
		return bac.restoreBanco(tempFile);
	}
	
	
	public String realizarBackupBancoDeDados() throws FuncionarioNaoAutorizadoException, IOException {
		Funcionario logado = ffunc.funcionarioComId(SessionUtil.getFuncionarioLogado().getId());
		PermissaoFuncionario.isAutorizado(logado, PermissaoFuncionario.ALTERAR_CONFIGURACOES);
		Backup bac = new Backup();
		return bac.criarBackup();
	}
	
	public void salvarConfiguracoesDeBackup(String nomeArquivoDestinoDefalt, boolean compactarBackup) throws IOException{
		Backup bac = new Backup();
		bac.salvarConfiguracoes(nomeArquivoDestinoDefalt, compactarBackup);
	}
	
	
	//Operacao de Risco
	public List<String> dividaSistemaAntigo() throws EntidadeNaoExistenteException, Exception{
		List<Cliente> clientes = fcli.listCelientes();
		List<String> msg = new ArrayList<String>();
		for(Cliente c : clientes){
			double deb = c.getDebito();
			List<Pagavel> di_ve = fv.pagavelNaoPagoDoCliente(c);
			double consta = 0;
			for(Pagavel p : di_ve){
				consta += p.getValorNaoPago();
			}
			if(deb > (consta + 0.09)){
				double dif = new BigDecimal(deb - consta).setScale(2,
								RoundingMode.HALF_UP).doubleValue();;
				Divida d = new Divida();
				d.setTotal(dif);
				d.setDescricao(OperacaoStringUtil.DESCRICAO_DIVIDA_ANTIGO_SISTEMA);
				d.setDia(Calendar.getInstance());
				d.getDia().set(2015, 8, 1);
				d.setCliente(c);
				vend.create(d);
				//System.out.println("Divida adicionada no cliente "+c.getNome()+" no valor de "+OperacaoStringUtil.formatarStringValorMoedaComDescricao(d.getValorNaoPago()));
				msg.add("Divida adicionada no cliente "+c.getNome()+" no valor de "+OperacaoStringUtil.formatarStringValorMoedaComDescricao(d.getValorNaoPago()));
			}
		}
		//System.out.println(msg.size() +" clientes do antigo sistema com debito.");
		return msg;
	}
	
	/* Apaga todas as vendas já pagas antes do dia informado 
	 * para melhorar o desempenho do banco de dados.
	 * */
	public int limparBancoDeDados(Date dia){
		return vend.limparBancoDeDados(dia);
	}
	
	
	public boolean imprimirVenda(Venda v){
		try{
			return imp.imprimirVenda(v);
		}catch(NullPointerException ne){
			return false;
		}
	}

	
}
