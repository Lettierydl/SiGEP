package com.twol.sigep.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Arquivo {
	public static String separador = System.getProperty("file.separator");
	public static  String os_name = System.getProperty("os.name");
	
	public static final File DIRETORIO_DEFALT = new File(
			"Arquivos_ClousSistem"+separador);
	public static final File CONFIGURACAO_BACKUP = new File(
			DIRETORIO_DEFALT+Arquivo.separador+"CONFIGURACAO_BACKUP.csi");

	public static void escrever(Object o, File arquivoDestino) throws IOException {
		if(!DIRETORIO_DEFALT.exists()){
			DIRETORIO_DEFALT.mkdirs();
		}
		FileOutputStream arquivoGrav = new FileOutputStream(
				arquivoDestino);
		ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
		objGravar.writeObject(o);
		
		objGravar.flush();
		objGravar.close();
		arquivoGrav.flush();
		arquivoGrav.close();
	}

	public static Object ler(File arquivo) throws IOException, ClassNotFoundException {
		FileInputStream arquivoLeitura = new FileInputStream(arquivo);
		ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
		Object o = objLeitura.readObject();
		
		objLeitura.close();
        arquivoLeitura.close();
		return o;
	}

}
