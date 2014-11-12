package br.com.staroski.jasper;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public final class Application {

	public static final String NAME;
	public static final String DESCRIPTION;
	public static final String URL;
	public static final BufferedImage ICON;
	public static final String VENDOR_URL;

	static {
		VENDOR_URL = "http://www.staroski.com.br";

		NAME = "JRPrint to PDF";
		DESCRIPTION = "Conversor de JRPrint para PDF";
		URL = VENDOR_URL + "/jrprint-to-pdf";

		ICON = load("/icone_128x128.png");
	}

	private static BufferedImage load(String resource) {
		try {
			return ImageIO.read(Application.class.getResourceAsStream(resource));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Application() {}
}
