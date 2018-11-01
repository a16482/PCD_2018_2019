package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

public class MeuLayoutCliente extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7587748315248715949L;
	private Container c;

	public MeuLayoutCliente(String str) {
		super(str);
		c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.WHITE);
	}

}
