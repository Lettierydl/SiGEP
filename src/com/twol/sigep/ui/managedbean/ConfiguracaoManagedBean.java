package com.twol.sigep.ui.managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.twol.sigep.Facede;
import com.twol.sigep.model.configuracoes.PermissaoFuncionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "confBean")
public class ConfiguracaoManagedBean {
	private Facede f;
	
	
	private String nomeEstabelecimento;

	public ConfiguracaoManagedBean() {
		f = new Facede();
	}
	
	public void alterarNomeDoEstabalecimento(){
		
	}
	
	
	
	
	
	
	
	public void configuracoesDefalt(){
		f.permissoesDeFuncionariosDefalt();
	}
	
	public boolean getPermissaoDeConfiguracao(){
		return SessionUtil.getFuncionarioLogado().getTipoDeFuncionario().equals(TipoDeFuncionario.Gerente);
	}

	public void setCadastroFuncCaixa(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_FUNCIONARIO, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getCadastroFuncCaixa(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_FUNCIONARIO, TipoDeFuncionario.Caixa);
	}
	
	public void setCadastroFuncSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_FUNCIONARIO, b, TipoDeFuncionario.Supervisor);
	}
	
	public boolean getCadastroFuncSupervisor(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_FUNCIONARIO, TipoDeFuncionario.Supervisor);
	}
	
	
	
	
	public void setCadastroCliCaixa(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_CLIENTES, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getCadastroCliCaixa(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_CLIENTES, TipoDeFuncionario.Caixa);
	}
	
	public void setCadastroCliSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_CLIENTES, b, TipoDeFuncionario.Supervisor);
	}
	
	public boolean getCadastroCliSupervisor(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_CLIENTES, TipoDeFuncionario.Supervisor);
	}
	
	
	public void setCadastroProdCaixa(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_PRODUTO, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getCadastroProdCaixa(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_PRODUTO, TipoDeFuncionario.Caixa);
	}
	
	public void setCadastroProdSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.CADASTRAR_PRODUTO, b, TipoDeFuncionario.Supervisor);
	}
	
	public boolean getCadastroProdSupervisor(){
		return f.getValor(PermissaoFuncionario.CADASTRAR_PRODUTO, TipoDeFuncionario.Supervisor);
	}
	
	
	
	public void setAlterarConfigSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, b, TipoDeFuncionario.Supervisor);
	}
	
	public boolean getAlterarConfigSupervisor(){
		return f.getValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, TipoDeFuncionario.Supervisor);
	}

	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nomeEstabelecimento) {
		this.nomeEstabelecimento = nomeEstabelecimento;
	}
	
	
	
	
	
}
