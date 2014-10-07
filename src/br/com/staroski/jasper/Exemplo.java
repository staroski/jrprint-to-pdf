package br.com.staroski.jasper;

import net.sf.jasperreports.engine.*;

public class Exemplo {

	public static void main(String[] args) {
		try {
			String jrprint = "caminho completo do arquivo .jrprint de origem";
			String pdf = "caminho completo do arquivo .pdf de destino";
			JasperExportManager.exportReportToPdfFile(jrprint, pdf);
			System.out.println("arquivo convertido com sucesso!");
		} catch (Throwable t) {
			System.out.println("erro ao converter arquivo:");
			t.printStackTrace();
		}
	}
}
