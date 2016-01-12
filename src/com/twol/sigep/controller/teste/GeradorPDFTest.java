package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerRelatorios;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.controller.gerador.GeradorPDF;
import com.twol.sigep.controller.gerador.GeradorPlanilha;
import com.twol.sigep.util.Backup;

public class GeradorPDFTest {
	
	GeradorPDF pdf;
	GeradorPlanilha pla;
	ControllerRelatorios cr;
	String nome = "Fulano de tal";
	FindFuncionario ffunc;
	
	@Before
	public void setup(){
		cr = new ControllerRelatorios();
		pdf = new  GeradorPDF(cr);
		pla = new GeradorPlanilha(cr);
		ffunc = new FindFuncionario();
	}
	
	
	/*
	 * PDF
	 */
	@Test
	public void createRelatorio() throws ParseException {
		pdf.gerarPdfRelatorioBalancoProdutos(getMinDia(), getMaxDia());
		
	}
	
	
	
	@Test
	public void createPlanilhaRelatorio() throws ParseException {
		System.out.println(pla.gerarPlanilhaRelatorioBalancoProdutos(getMinDia(), getMaxDia()) );
		
	}
	
	
	@Test
	public void backupTeste() throws ParseException, IOException {
		Backup m = new Backup();
		m.criarBackup();
	}
	
	
	@Test
	public void restoreTeste() throws ParseException, IOException {
		Backup m = new Backup();
		m.restoreBanco(new File(m.getNomeArquivoDestinoDefalt()));
		assertEquals(ffunc.funcionarioComId(2).getNome(), "Amanda");
	}
	
	
	
	public Date getMaxDia() {
		return new Date();
	}

	public Date getMinDia() throws ParseException {
		return new SimpleDateFormat("dd/MM/yyyy").parse(cr.getMinDia());
	}
	
	
}
