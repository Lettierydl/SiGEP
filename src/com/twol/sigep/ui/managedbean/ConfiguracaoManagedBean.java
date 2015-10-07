package com.twol.sigep.ui.managedbean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.twol.sigep.Facede;
import com.twol.sigep.model.configuracoes.PermissaoFuncionario;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.MySQLBackup;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "confBean")
public class ConfiguracaoManagedBean {
	private Facede f;
	
	private UploadedFile file;
	private File tempFile;
	private StreamedContent streamed;
	private int mesesAnteriores = 3 ; // meses anteriaores recomendados para nao deletar as vendas
	
	private Date dataLimpesa;
    
	
	private String nomeEstabelecimento;

	public ConfiguracaoManagedBean() {
		f = new Facede();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-mesesAnteriores);
		dataLimpesa = c.getTime();
	}
	
	public void alterarNomeDoEstabalecimento(){
		
	}
	
	public void abrirMoralLimparBanco(){
		RequestContext.getCurrentInstance().execute(
				"abrirModa('modalLimpesa');");
	}
	
	public void limparBanco(){
		try{
		int porc = f.limparBancoDeDados(dataLimpesa);
		SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Limpesa do banco realizada","Vendas foram limberado "+porc+"% das vendas"));
		}catch(Exception e){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Não foi possivel efetuar a limpesa","Não foi possivel efetuar a limpesa"));
		
		}
	}
	
	
	public void naolimparBanco(){
		RequestContext.getCurrentInstance().execute(
				"fecharModal('modalLimpesa');");
	}
	
	public void realizarBackup(){
		try {
			new MySQLBackup();
			String path = f.realizarBackupBancoDeDados(new File(MySQLBackup.defalt));
			
			InputStream stream = new FileInputStream(path);
			streamed = new DefaultStreamedContent(stream, "application/pdf", "Relatorio_Balanco_Produto.pdf");
		    RequestContext.getCurrentInstance().update("@form");
		    SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Backup realizado","Arquivo: "+path));
		
		    return;
			
		
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe.getMessage()));
		} catch (IOException e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Backup não pode ser salvo","Backup não pode ser salvo"));
		}
	}
	
	
	public void restoreBancoDeDados(FileUploadEvent event) throws IOException {
		file = event.getFile();
		
		tempFile = File.createTempFile("TempBackupCloudSistem", ".sql");
        OutputStream temp = new FileOutputStream(tempFile);
        IOUtils.copy(event.getFile().getInputstream(), temp);
		
		
		if(event.getFile() == null){
			FacesMessage message = new FacesMessage("Aquivo inválido", "Não foi possivel carregar o arquivo "+event.getFile().getFileName());
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}else{
			RequestContext.getCurrentInstance().execute(
					"abrirModa('modalRestore');");
		}
	}
	
	public void restaurar(){
		try {
			f.resteurarBancoDeDados(tempFile);
			SessionUtil.redirecionarParaPage(SessionUtil.PAGE_INICIAL);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Arquivo com dádos inválidos", "Arquivo com dádos inválidos"));
		}
		
	}
	
	public void naoRestaurar(){
		RequestContext.getCurrentInstance().execute(
				"fecharModal('modalRestore');");
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

	
	public void setAlterarCliSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_CLIENTES, b, TipoDeFuncionario.Supervisor);
	}
	public void setAlterarCliCaixa(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_CLIENTES, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getAlterarCliSupervisor(){
		return f.getValor(PermissaoFuncionario.ALTERAR_CLIENTES,  TipoDeFuncionario.Supervisor);
	}
	public boolean getAlterarCliCaixa(){
		return f.getValor(PermissaoFuncionario.ALTERAR_CLIENTES,  TipoDeFuncionario.Caixa);
	}
	

	public void setAlterarFuncSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_FUNCIONARIO, b, TipoDeFuncionario.Supervisor);
	}
	public void setAlterarFuncCaixa(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_FUNCIONARIO, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getAlterarFuncSupervisor(){
		return f.getValor(PermissaoFuncionario.ALTERAR_FUNCIONARIO,  TipoDeFuncionario.Supervisor);
	}
	public boolean getAlterarFuncCaixa(){
		return f.getValor(PermissaoFuncionario.ALTERAR_FUNCIONARIO,  TipoDeFuncionario.Caixa);
	}
	
	
	
	
	public void setAlterarProdSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_PRODUTO, b, TipoDeFuncionario.Supervisor);
	}
	public void setAlterarProdCaixa(boolean b){
		f.putValor(PermissaoFuncionario.ALTERAR_PRODUTO, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getAlterarProdSupervisor(){
		return f.getValor(PermissaoFuncionario.ALTERAR_PRODUTO,  TipoDeFuncionario.Supervisor);
	}
	public boolean getAlterarProdCaixa(){
		return f.getValor(PermissaoFuncionario.ALTERAR_PRODUTO,  TipoDeFuncionario.Caixa);
	}
	
	

	
	public void setGerarRelSupervisor(boolean b){
		f.putValor(PermissaoFuncionario.GERAR_RELATORIOS, b, TipoDeFuncionario.Supervisor);
	}
	public void setGerarRelCaixa(boolean b){
		f.putValor(PermissaoFuncionario.GERAR_RELATORIOS, b, TipoDeFuncionario.Caixa);
	}
	
	public boolean getGerarRelSupervisor(){
		return f.getValor(PermissaoFuncionario.GERAR_RELATORIOS,  TipoDeFuncionario.Supervisor);
	}
	public boolean getGerarRelCaixa(){
		return f.getValor(PermissaoFuncionario.GERAR_RELATORIOS,  TipoDeFuncionario.Caixa);
	}
	
	public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
	
	
	
	
	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nomeEstabelecimento) {
		this.nomeEstabelecimento = nomeEstabelecimento;
	}

	public StreamedContent getStreamed() {
		return streamed;
	}

	public void setStreamed(StreamedContent streamed) {
		this.streamed = streamed;
	}

	public Date getDataLimpesa() {
		return dataLimpesa;
	}

	public void setDataLimpesa(Date dataLimpesa) {
		this.dataLimpesa = dataLimpesa;
	}
	
	
	public String getMaxDia() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-2);
		return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
	}
	
	public String getDiaRecomentado() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-mesesAnteriores);
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	}
	
	public String getMinDia() {
		return f.getMinDiaRegistroVenda();
	}
	
	
}
