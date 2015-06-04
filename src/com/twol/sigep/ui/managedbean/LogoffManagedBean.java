package com.twol.sigep.ui.managedbean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "logoffBean")
public class LogoffManagedBean {
	private Facede f;

	public LogoffManagedBean() {
		f = new Facede();
	}
	
	public void logoff() throws IOException{
		f.logoff();
		SessionUtil.redirecionarParaPage(SessionUtil.PAGE_INICIAL);
	}
	
	public void nao(){
		RequestContext.getCurrentInstance().execute(
				"fecharModal('modalLogoff');");
	}
	
	
}
