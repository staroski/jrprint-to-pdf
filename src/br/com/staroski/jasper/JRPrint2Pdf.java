package br.com.staroski.jasper;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.*;

/**
 * Classe utilitária para converter arquivos JRPRINT para PDF
 * 
 * @author Ricardo Artur Staroski
 */
public class JRPrint2Pdf extends JFrame {

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			executaModoGrafico();
		} else {
			try {
				converter(args[0], args[1]);
				System.out.println("arquivo convertido com sucesso!");
			} catch (Throwable e) {
				System.out.println("erro ao converter arquivo:");
				e.printStackTrace();
			}
		}
	}

	private static void converter(String jrprint, String pdf) throws Throwable {
		JasperExportManager.exportReportToPdfFile(jrprint, pdf);
	}

	private static void executaModoGrafico() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		try {
			JFrame janela = new JRPrint2Pdf();
			janela.setVisible(true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static final String JRPRINT = ".jrprint";
	private static final String PDF = ".pdf";
	private static final long serialVersionUID = 1;

	private JFileChooser chooser;
	private JButton botaoEntrada;
	private JTextField campoEntrada;
	private JButton botaoSaida;
	private JTextField campoSaida;
	private JButton converter;
	private JButton fechar;

	public JRPrint2Pdf() {
		super(Application.NAME + " - " + Application.VENDOR_URL);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(Application.ICON);
		JPanel container = new JPanel();
		setContentPane(container);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				trataFechamento();
			}
		});

		JPanel entrada = montaPainelEntrada();
		JPanel saida = montaPainelSaida();
		JPanel botoes = montaPainelBotoes();

		container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		container.setLayout(new GridLayout(3, 1));
		container.add(entrada);
		container.add(saida);
		container.add(botoes);

		Dimension size = new Dimension(640, 180);
		setSize(size);
		setResizable(false);
		setMinimumSize(size);
		setLocationRelativeTo(null);
	}

	private JFileChooser getChooser() {
		if (chooser == null) {
			chooser = new JFileChooser(System.getProperty("user.dir"));
		}
		return chooser;
	}

	private void habilitar(boolean habilitar) {
		botaoEntrada.setEnabled(habilitar);
		campoEntrada.setEnabled(habilitar);
		botaoSaida.setEnabled(habilitar);
		campoSaida.setEnabled(habilitar);
		converter.setEnabled(habilitar);
		fechar.setEnabled(habilitar);
	}

	private JPanel montaPainelBotoes() {
		converter = new JButton("Converter");
		converter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trataConversao();
			}
		});

		fechar = new JButton("Fechar");
		fechar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trataFechamento();
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
		panel.add(converter);
		panel.add(fechar);
		return panel;
	}

	private JPanel montaPainelEntrada() {
		campoEntrada = new JTextField();

		botaoEntrada = new JButton("...");
		botaoEntrada.setToolTipText("Selecionar arquivo JRPrint");
		botaoEntrada.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trataSelecaoEntrada();
			}
		});

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(BorderLayout.NORTH, new JLabel("Arquivo JRPrint de entrada:"));
		painel.add(BorderLayout.CENTER, campoEntrada);
		painel.add(BorderLayout.EAST, botaoEntrada);
		painel.setPreferredSize(new Dimension(400, 80));
		return painel;
	}

	private JPanel montaPainelSaida() {
		campoSaida = new JTextField();

		botaoSaida = new JButton("...");
		botaoSaida.setToolTipText("Selecionar arquivo PDF");
		botaoSaida.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trataSelecaoSaida();
			}
		});

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(BorderLayout.NORTH, new JLabel("Arquivo PDF de saída:"));
		painel.add(BorderLayout.CENTER, campoSaida);
		painel.add(BorderLayout.EAST, botaoSaida);
		painel.setPreferredSize(new Dimension(400, 80));
		return painel;
	}

	private void trataConversao() {
		String entrada = campoEntrada.getText().trim();
		String saida = campoSaida.getText().trim();
		if (entrada.isEmpty() && saida.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o arquivo " + JRPRINT + " a ser convertido e o arquivo " + PDF + " a ser gerado!", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (entrada.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o arquivo " + JRPRINT + " a ser convertido!", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (saida.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o arquivo " + PDF + " a ser gerado!", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		final Cursor cursor = getCursor();
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			habilitar(false);
			converter(entrada, saida);
			JOptionPane.showMessageDialog(this, "Arquivo convertido com sucesso!", "Concluído", JOptionPane.INFORMATION_MESSAGE);
		} catch (Throwable e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
		} finally {
			setCursor(cursor);
			habilitar(true);
		}
	}

	private void trataFechamento() {
		int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (opcao == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void trataSelecaoEntrada() {
		JFileChooser chooser = getChooser();
		chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(JRPRINT);
			}

			@Override
			public String getDescription() {
				return "JasperReports Print Document ( *" + JRPRINT + " )";
			}
		});
		int opcao = chooser.showOpenDialog(this);
		if (opcao != JFileChooser.CANCEL_OPTION) {
			File arquivo = chooser.getSelectedFile();
			String caminho = arquivo.getAbsolutePath();
			if (!caminho.toLowerCase().endsWith(JRPRINT)) {
				caminho += JRPRINT;
			}
			campoEntrada.setText(caminho);

			if (campoSaida.getText().trim().isEmpty()) {
				campoSaida.setText(caminho.substring(0, caminho.lastIndexOf(JRPRINT)) + PDF);
			}
		}
	}

	private void trataSelecaoSaida() {
		JFileChooser chooser = getChooser();
		chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(PDF);
			}

			@Override
			public String getDescription() {
				return "Portable Document Format ( *" + PDF + " )";
			}
		});
		int opcao = chooser.showSaveDialog(this);
		if (opcao != JFileChooser.CANCEL_OPTION) {
			File arquivo = chooser.getSelectedFile();
			String caminho = arquivo.getAbsolutePath();
			if (!caminho.toLowerCase().endsWith(PDF)) {
				caminho += PDF;
			}
			campoSaida.setText(caminho);
		}
	}
}
