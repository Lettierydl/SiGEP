import com.twol.sigep.model.exception.EntidadeJaPersistidaException;
import com.twol.sigep.model.exception.EstadoInvalidoDaVendaAtualException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.exception.ProdutoABaixoDoEstoqueException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.Persistencia;





public class MainTeste {

	/**
	 * @param args
	 * @throws EntidadeJaPersistidaException 
	 * @throws ParametrosInvalidosException 
	 * @throws PermissaoInvalidaException 
	 * @throws InterruptedException 
	 * @throws VendaPendenteException 
	 * @throws ProdutoABaixoDoEstoqueException 
	 * @throws EstadoInvalidoDaVendaAtualException 
	 */
	public static void main(String[] args) throws EntidadeJaPersistidaException, ParametrosInvalidosException, PermissaoInvalidaException, InterruptedException, VendaPendenteException, ProdutoABaixoDoEstoqueException, EstadoInvalidoDaVendaAtualException{
		Funcionario f = Funcionario.recuperarLista().get(0);
		System.out.println(f);
		Funcionario.remover(f);
	}	
	
		/*
		 * 
		Cliente c = Cliente.recuperarCliente(2);
		Funcionario logado = Funcionario.recuperarFuncionarioPorLoginESenha("leo", "123456");
		Produto p1 = Produto.recuperarProduto(3);
		Produto p2 = Produto.recuperarProduto(2);
		Produto p3 = Produto.recuperarProduto(1);
		
		
		ControllerVenda cv = new ControllerVenda(logado);
		
		cv.iniciarNovaVenda();
		cv.addLinhaDaVenda(p1, 3);
		cv.addLinhaDaVenda(p2, 7);
		cv.addLinhaDaVenda(p3, 9.6);
		
		cv.finalizarVenda(c, null, FormaDePagamento.A_Prazo);
	
		 */
	
		/*Venda v = new Venda();
		Cliente c = Cliente.recuperarCliente(1);
		Funcionario logado = Funcionario.recuperarFuncionarioPorLoginESenha("leo", "123456");
		Produto p1 = Produto.recuperarProduto(3);
		Produto p2 = Produto.recuperarProduto(2);
		Produto p3 = Produto.recuperarProduto(1);
		
		
		ControllerVenda cv = new ControllerVenda(logado);
		
		cv.iniciarNovaVenda();
		cv.addLinhaDaVenda(p1, 3);
		cv.addLinhaDaVenda(p2, 7);
		cv.addLinhaDaVenda(p3, 9.6);
		
		cv.finalizarVenda(c, null, FormaDePagamento.A_Vista);
	
	
	/*
	 * Funcionario f = new Funcionario();
		f.setCpf("000000000");
		f.setNome("zé");
		f.setLogin("ze");
		
		Endereco e = new Endereco();
		e.setBairro("Bairro");
		e.setCep("88888999");
		e.setCidade("João Pessoa");
		e.setNumero("numero");
		e.setRua("rua");
		e.setUf(UF.PB);
		f.setEndereco(e);
		
		Telefone t = new Telefone();
		t.setDdd("00");
		t.setTelefone("00000000");
		t.setOperadora("OPERADORA");
		
		f.addTelefone(t);
		new ControllerFuncionario().salvarFuncionario(f, "123456", TipoDeFuncionario.Caixa);
		
	 * */
	
	
	
	
	/*Teste FinderProduto
	 * Produto p = new Produto();
		p.setCategoria(CategoriaProduto.Produtos_de_Higiene);
		p.setCodigoDeBarras("1234567890");
		p.setDescricao("Produto Teste de Descricao");
		p.setLimiteMinimoEmEstoque(10);
		p.setValorDeCompra(30);
		p.setValorDeVenda(40);
		try{
			Produto.salvar(p);
		}catch(Exception e){}
		
		System.out.println(FinderProduto
				.produtosQueDescricaoOuCodigoDeBarrasIniciam(
						null, "123"
						));
	 */
	
	
	/*Teste Relacionamento Venda e produto
	Venda v = new Venda();
	
	v.setDia(Calendar.getInstance());
	
	LinhaDaVenda lv = new LinhaDaVenda();
	lv.setQuantidadeVendida(10);
	lv.setProduto(Produto.recuperarProduto(1));
	v.addLinhaDaVenda(lv);
	
	
	LinhaDaVenda lv2 = new LinhaDaVenda();
	lv2.setQuantidadeVendida(8.9);
	lv2.setProduto(Produto.recuperarProduto(2));
	v.addLinhaDaVenda(lv2);
	
	Venda.salvar(v);
	
	
	
	Produto p = new Produto();
	p.setCategoria(CategoriaProduto.Alimentos);
	p.setCodigoDeBarras("12340235678");
	p.setDescricao("Produto Teste 2");
	p.setValorDeVenda(11.3);
	p.setValorDeCompra(9.9);
	
	Promocao pr = new Promocao();
	pr.setDataDeInicio(Calendar.getInstance());
	pr.setValorDoDesconto(2.3);
	p.addPromocaoValida(pr);
	
	Produto.salvar(p);
	*/
	
	
	/*Pagamento
	 * Funcionario f = new Funcionario();
	f.setCpf("0j988ihj");
	f.setNome("zéDoido");
	f.setLogin("zeDoido");
	
	new ControllerFuncionario().salvarFuncionario(f, "123456", TipoDeFuncionario.Caixa);
	
	Cliente c = new Cliente();
	c.setCpf("9nk232323");
	c.setNome("Cliente Teste");
	c.setDataDeNascimento(Calendar.getInstance());
	Cliente.salvar(c);
	
	//Pagamento p = new Pagamento(100, c, f);
	Cliente c = Cliente.recuperarCliente(1);
	Funcionario f = Funcionario.recuperarFuncionarioPorLoginESenha("leo", "123456");
	Pagamento p = new Pagamento(230.00, c, f);
	Pagamento.salvar(p);
	//System.out.println(p.getData().get(Calendar.SECOND)+ " id:" + p.getId());
	
	//System.out.println(Pagamento.recuperarPagamentoPorID(p.getId()).getData().get(Calendar.SECOND));
	
	//System.out.println(f.getPagamentos());
	
	
	
	//System.out.println(Pagamento.recuperarPagamentoPorID(p.getId()));
	*/
}
