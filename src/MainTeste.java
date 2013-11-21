import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.FinderProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeJaPersistidaException;




public class MainTeste {

	/**
	 * @param args
	 * @throws EntidadeJaPersistidaException 
	 */
	public static void main(String[] args) throws EntidadeJaPersistidaException {
		Produto p = new Produto();
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
		
	}

	
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
	
	
	
}
