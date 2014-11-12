package br.com.staroski.jasper;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.jnlp.*;
import javax.swing.*;
import javax.swing.border.*;

public final class ProgressWindow implements DownloadServiceListener {

	/**
	 * A JNLP client's DownloadService implementation should call this method if a download fails or aborts unexpectedly.
	 */
	public void downloadFailed(URL url, String version) {}

	/**
	 * A JNLP client's DownloadService implementation should call this method several times during a download.
	 */
	public void progress(URL url, String version, long readSoFar, long total, int overallPercent) {
		updateProgressUI(url, overallPercent);
	}

	/**
	 * A JNLP client's DownloadService implementation should call this method at least several times when applying an incremental update to an in-cache
	 * resource.
	 */
	public void upgradingArchive(URL url, String version, int patchPercent, int overallPercent) {
		updateProgressUI(url, overallPercent);
	}

	/**
	 * A JNLP client's DownloadService implementation should call this method at least several times during validation of a download.
	 */
	public void validating(URL url, String version, long entry, long total, int overallPercent) {
		updateProgressUI(url, overallPercent);
	}

	private JFrame frame;
	private JLabel label;
	private JProgressBar progressBar;
	private boolean uiCreated;

	private void create() {
		try {
			// tentar deixar a aplicação com aspecto nativo do sistema operacional
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		frame = new JFrame(Application.NAME);
		frame.setIconImage(Application.ICON);
		frame.setUndecorated(true);
		frame.setSize(480, 240);
		frame.setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		frame.setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel2 = new JPanel();
		panel2.setOpaque(false);
		panel2.setLayout(new BorderLayout(0, 0));

		label = new JLabel();

		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(100, 30));
		panel2.add(label, BorderLayout.NORTH);
		panel2.add(progressBar, BorderLayout.CENTER);

		panel.add(panel2, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel(Application.DESCRIPTION);
		lblNewLabel.setFont(new Font("Arial", lblNewLabel.getFont().getStyle() | Font.BOLD | Font.ITALIC, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel, BorderLayout.CENTER);

		JLabel lblVendorUrl = new JLabel(Application.URL);
		lblVendorUrl.setForeground(Color.BLUE);
		lblVendorUrl.setFont(new Font("Arial", lblVendorUrl.getFont().getStyle() | Font.BOLD | Font.ITALIC, 12));
		lblVendorUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVendorUrl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblVendorUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				try {
					Desktop.getDesktop().browse(new URI(Application.URL));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(lblVendorUrl, BorderLayout.SOUTH);
		updateProgressUI(null, 0);
	}

	private void updateProgressUI(URL url, int overallPercent) {
		if (overallPercent > 0 && overallPercent < 99) {
			if (!uiCreated) {
				uiCreated = true;
				create();
			}
			if (url != null) {
				String file = url.getFile();
				file = file.substring(file.lastIndexOf('/') + 1);
				label.setText("Carregando \"" + file + "\"...");
			}
			progressBar.setValue(overallPercent);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.setVisible(true);
				}
			});
		} else {
			if (uiCreated) {
				// hide frame when overallPercent is above 99
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.setVisible(false);
						frame.dispose();
					}
				});
			}
		}
	}
}
