package pt.iscte.P2PDownload.TheISCTEBay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class PopUpActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent evt) {
		System.out.println("Selected: " + evt.getActionCommand());
		//...
		switch(evt.getActionCommand()) {
		case ("Utilizadores"):
			criaEmostraIGUtilizadores();
		break;
		case ("Configurações"):
			break;
		default: 
			// nada para fazer...
			break;
		}
	}
	
	private static void criaEmostraIGUtilizadores() {
		JFrame frame = new JFrame("Utilizadores");
		frame.setAlwaysOnTop(true);
		JComponent UtilizadoresContentPane = new IGUtilizadores();
		UtilizadoresContentPane.setOpaque(true); 
		frame.setContentPane(UtilizadoresContentPane);
		frame.pack();
		frame.setSize(400, 400);
		frame.setResizable(false);
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@SuppressWarnings("unused")
	private static void criaEmostraIGConfig() {
		// to do
	}
}
