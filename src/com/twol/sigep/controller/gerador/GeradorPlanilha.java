package com.twol.sigep.controller.gerador;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.itextpdf.text.DocumentException;
import com.twol.sigep.controller.ControllerRelatorios;

public class GeradorPlanilha {

	private String arquivo = "/Planilhas/";
	private ControllerRelatorios cr;

	public GeradorPlanilha(ControllerRelatorios cr) {
		this.cr = cr;
	}

	public String gerarPlanilhaRelatorioBalancoProdutos(Date inicio, Date fim) {
		
		arquivo = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + arquivo;
		
		File f = new File(arquivo);
		f.mkdir();
		
		try {
			
			String path = f.getCanonicalPath() + "/PlanilhaBalancoProdutos.xls";
			WritableWorkbook workbook = Workbook.createWorkbook(new File(path)); 
			WritableSheet folha =  workbook.createSheet("Relatorio Produtos", 0); 
			
			
			
			String subTitulo = "Saída de produtos \n Referênte do dia "+
					new SimpleDateFormat("dd/MM/yyyy").format(inicio)+ " ao dia "+new SimpleDateFormat("dd/MM/yyyy").format(fim);
			inserirHead(folha, "Balanço de Produtos", subTitulo);

			addDadosRelatorioProduto(folha ,inicio, fim);
			
			
			workbook.write();
			workbook.close();
			
			return path;
		} catch (DocumentException | IOException | WriteException e) {
			e.printStackTrace();
			return "";
		}
		
		

	}

	private void addDadosRelatorioProduto(WritableSheet folha,Date inicio, Date fim) throws DocumentException, RowsExceededException, WriteException {
		Map<String, Double[]> saida = cr.getRelatorioDetalhadoSaidaProduto(
				inicio, fim);
		WritableCellFormat cour16 = new WritableCellFormat (new WritableFont(WritableFont.COURIER, 16));
		WritableCellFormat cour14 = new WritableCellFormat (new WritableFont(WritableFont.COURIER, 14));
		
		double compra = 0, venda = 0, lucro = 0, qt = 0;
		int row = 7, colun=0;
		folha.addCell(new Label(colun++,row,"Produto",cour14));
		folha.addCell(new Label(colun++,row,"Compra",cour14));
		folha.addCell(new Label(colun++,row,"Venda",cour14));
		folha.addCell(new Label(colun++,row,"Lucro",cour14));
		folha.addCell(new Label(colun++,row++,"Estoque",cour14));
		for(String produto: saida.keySet()){
			colun = 0;
			folha.addCell(new Label(colun++,row,produto,cour14));
			
			Double[] val = saida.get(produto);
			compra += val[0]; venda += val[1]; lucro += val[3];qt += val[2];
			
			folha.addCell(new Number(colun++,row, val[0]));
			folha.addCell(new Number(colun++,row, val[1]));
			folha.addCell(new Number(colun++,row, val[3]));
			folha.addCell(new Number(colun++,row++, val[2]));
		}
		row++;colun = 0;
		folha.addCell(new Label(colun,++row,"Total:",cour16));
		
		folha.addCell(new Label(colun++,row,saida.size()+" Produtos",cour14));
		
		folha.addCell(new Number(colun++,row, compra));
		folha.addCell(new Number(colun++,row, venda));
		folha.addCell(new Number(colun++,row, lucro));
		folha.addCell(new Number(colun++,row++, qt));

	}

	
	public void inserirHead(WritableSheet folha, String titulo, String subTitulo) {
		try {
			String path = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("imagens");
			
			
			WritableImage img = new WritableImage(0, 0,2, 6, new File(path+"/logo_relatorio.png"));
			
			folha.addImage(img);
			
			WritableCellFormat cour20 = new WritableCellFormat (new WritableFont(WritableFont.COURIER, 20));
			WritableCellFormat cour16 = new WritableCellFormat (new WritableFont(WritableFont.COURIER, 16));
			
			Label title = new Label(0,7,titulo, cour20);
			Label subtitle = new Label(0,7,subTitulo, cour16);
			
			folha.addCell(title);
			folha.addCell(subtitle);
			

		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
}
