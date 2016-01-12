package com.twol.sigep.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class MySQLBackup {

	private static final String osname = System.getProperty("os.name");
	public static String defalt;
	public static final String separador = System.getProperty("file.separator");
	public static String mysqldump = "C:"+separador+"Program Files"+separador+"MySQL"+separador+"MySQL Server 5.5"+separador+"bin"+separador+"mysqldump.exe";
    public static String mysql = "C:"+separador+"Program Files"+separador+"MySQL"+separador+"MySQL Server 5.5"+separador+"bin"+separador+"mysql.exe";
    
    String base = "cloudsistem";
    
	private static String usuario = "root";
	private static String senha = "";

	
	public MySQLBackup(){
		if (osname.startsWith("Windows")) {
            defalt = System.getProperty("user.dir").substring(0, 3);
        } else {
            defalt = "/Users/" + System.getProperty("user.name");
            mysqldump = "/usr/local/mysql/bin/mysqldump";
            mysql = "/usr/local/mysql/bin/mysql";
        }
	}
	
	
	/**
	 * Exporta para um arquivo os dados de uma base de dados específica.
	 * 
	 * @param arquivo
	 *            Caminho do arquivo que deve ser gerado com a saída da
	 *            exportação.
	 * @param usuario
	 *            Usuário usado para conectar na base.
	 * @param senha
	 *            Senha do usuário.
	 * @param base
	 *            Nome da base a ser exportada.
	 * @param tabelas
	 *            Tabelas da base especifica a serem exportadas. Caso não seja
	 *            expecificada nenhuma, todas as tabelas serão exportadas.
	 * @throws IOException
	 */
	public String dump(File file) throws IOException {

		String arquivo = file.getCanonicalPath();
		
		// prepara o comando
		StringBuilder sbComando = new StringBuilder(mysqldump);

		sbComando.append(" -u");
		sbComando.append(usuario);

		/*
		 * Se o valor da senha for diferente de vazio, adiciona o parametro de
		 * senha, caso contrário não adiciona, pois pode gerar um arquivo vazio
		 */
		if (senha != "") {
			// o valor do parâmetro -p não tem espaço!
			sbComando.append(" -p");
			sbComando.append(senha);
		}

		sbComando.append(" ");
		sbComando.append(base+" ");
		
		arquivo  += "/cloudsistem_" +new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + ".sql";
		
		sbComando.append(" --result-file=");
		sbComando.append(arquivo);

				// executa o comando
		Runtime.getRuntime().exec(sbComando.toString());

		
		return arquivo;
	}

	/**
	 * Restaura os dados de um script sql para base de dados.
	 * 
	 * @param fileTemp
	 *            Caminho do arquivo que será restaurado
	 * @param host
	 *            Servidor onde se encontra a base de dados.
	 * @param porta
	 *            Porta utilizada para conectar com o host.
	 * @param usuario
	 *            Usuário usado para conectar na base.
	 * @param senha
	 *            Senha do usuário.
	 * @param base
	 *            Nome da base a ser restaurada.
	 * @throws IOException
	 */
	public void restore(File fileTemp) throws IOException {


    	// prepara o comando
		StringBuilder sbComando = new StringBuilder(mysql);

		
		sbComando.append(" -u");
		sbComando.append(usuario);

		/*
		 * Se o valor da senha for diferente de vazio, adiciona o parametro de
		 * senha, caso contrário não adiciona, pois pode gerar um arquivo vazio
		 */
		if (!senha.isEmpty()) {
			// o valor do parâmetro -p não tem espaço!
			sbComando.append(" -p");
			sbComando.append(senha);
		}

		sbComando.append(" ");
		sbComando.append(base);

		sbComando.append(" < ");//--result-file=
		sbComando.append(fileTemp.getCanonicalPath());

		// Tempo
		long t1, t2, tempo;

		System.out.println("Processo de Restauração Iniciado...");
		// imprime o comando
		System.out.println(sbComando.toString());
		System.out.println(sbComando.toString());

		// tempo inicio
		t1 = System.currentTimeMillis();
		
		// executa o comando
		Runtime.getRuntime().exec(sbComando.toString());
		
		
		// tempo fim
		t2 = System.currentTimeMillis();

		// Tempo total da operação
		tempo = t2 - t1;
		System.out.println("Processo de Restauração Executado em: " + tempo
				+ "ms");
		System.out.println("Processo de Restauração Finalizado!");
	}
	
	
	
}

class StreamGobbler implements Runnable {
	private InputStream is;
	private String type;
	private FileWriter fw;

	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public StreamGobbler(InputStream is, String type, File file)
			throws IOException {
		this.is = is;
		this.type = type;
		this.fw = new FileWriter(file);
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (fw != null) {
					fw.write(line + "\n");
				} else {
					System.out.println(type + ">" + line);
				}
			}
			if (fw != null) {
				fw.close();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
